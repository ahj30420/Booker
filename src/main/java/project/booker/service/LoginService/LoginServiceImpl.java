package project.booker.service.LoginService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.LoginController.dto.LoginDto;
import project.booker.controller.LoginController.dto.AccessTokenDto;
import project.booker.domain.Member;
import project.booker.controller.LoginController.dto.JoinDto;
import project.booker.domain.social.Social;
import project.booker.dto.AuthenticatedUser;
import project.booker.exception.exceptions.DuplicatedIDException;
import project.booker.exception.exceptions.InvalidRefreshTokenException;
import project.booker.repository.LoginRepository;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.util.jwt.JwtProvider;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 일반 회원가입
     * 비밀번호는 보안에 중요한 개인 정보이므로 데이터베이스에 저장하기 전에 암호화!!
     */
    @Transactional
    public Long NomarlJoin(JoinDto joinDto){

        //아이디 중복 체크
        ValidateDuplicateId(joinDto);

        String id = joinDto.getId();

        //pw 암호화
        String pw = bCryptPasswordEncoder.encode(joinDto.getPw());

        String name = joinDto.getName();
        String email = joinDto.getEmail();
        LocalDate birth = joinDto.getBirth();
        Social social = Social.NORMAL;
        LocalDate date = LocalDate.now();

        Member member = Member.createMember(id,pw,name,email,birth,social,date);

        loginRepository.save(member);

        return member.getMemberIdx();
    }

    /**
     * 로그인 시 ID, PW로 회원 검증
     */
    @Transactional
    public Member VerifyUser(LoginDto loginDto) {

        Member VerifyUser = loginRepository.findLoginByIdAndSocial(loginDto.getId(),Social.NORMAL);
        if(VerifyUser == null || !bCryptPasswordEncoder.matches(loginDto.getPw(), VerifyUser.getPw())){
            return null;
        }

        return VerifyUser;
    }

    /**
     * refresh 토큰 저장하기
     */
    @Transactional
    public void UpdateRefreshToken(Long idx, String refreshToken){

        Member member = loginRepository.findById(idx).get();
        if(member == null){
            return;
        }

        member.updateRefreshToken(refreshToken);
    }

    /**
     * refreshToken으로 accessToken 갱신하기
     * 1. refeshToken 유효성 검사
     * 2. refresToken으로 회원 조회
     * 3. 조회된 회원 정보로 새로운 AccessToken 발급
     */
    public AccessTokenDto refreshToken(String refreshToken){
        try{
            //JWT 유효성 검사
            jwtProvider.getClaims(refreshToken);
            Member member = loginRepository.findByRefreshToken(refreshToken);
            if (member == null) {
                throw new NullPointerException();
            }

            Map<String, Object> NewClaims = new HashMap<>();
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(member.getMemberIdx(), member.getName(),member.getMemberProfile().getNickname());
            String authenticatedUserJson = objectMapper.writeValueAsString(authenticatedUser);
            NewClaims.put("AuthenticetedUser", authenticatedUserJson);
            AccessTokenDto accessToken = jwtProvider.createAccessToken(NewClaims);
            return accessToken;
        } catch (Exception e) {
            throw new InvalidRefreshTokenException(ErrorCode.INVALID_RefreshToken);
        }
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 일반 회원가입 시 아이디 중복 체크 메서드
     */
    private void ValidateDuplicateId(JoinDto form) {
        Optional.ofNullable(loginRepository.findDuplicatedIDByIdAndSocial(form.getId(), Social.NORMAL))
                .ifPresent(user -> {
                    throw new DuplicatedIDException(ErrorCode.DUPLICATED_USER_ID);
                });
    }

}
