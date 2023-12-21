package project.booker.service.loginService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.LoginController.dto.request.LoginRequestDto;
import project.booker.domain.Member;
import project.booker.controller.LoginController.dto.request.JoinRequestDto;
import project.booker.dto.AuthenticatedUser;
import project.booker.exception.exceptions.DuplicatedIDException;
import project.booker.repository.LoginRepository;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.util.jwt.Jwt;
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
     */
    @Transactional
    public void NomarlJoin(JoinRequestDto joinRequestDto){

        ValidateDuplicateId(joinRequestDto);

        String id = joinRequestDto.getId();

        //pw는 민감한 정보이기 때문에 암호화해서 DB에 저장합니다.
        String pw = bCryptPasswordEncoder.encode(joinRequestDto.getPw());

        String name = joinRequestDto.getName();
        String email = joinRequestDto.getEmail();
        LocalDate birth = joinRequestDto.getBirth();
        String social = "normal";
        LocalDate date = LocalDate.now();

        Member member = Member.createMember(id,pw,name,email,birth,social,date);

        loginRepository.save(member);
    }

    /**
     * 로그인 시 ID, PW로 회원 검증
     */
    @Transactional
    public Member VerifyUser(LoginRequestDto loginRequestDto) {

        Member VerifyUser = loginRepository.findLoginByIdAndSocial(loginRequestDto.getId(),"normal");
        if((VerifyUser == null) || bCryptPasswordEncoder.matches(loginRequestDto.getPw(), VerifyUser.getPw())){
            return null;
        }

        return VerifyUser;
    }

    /**
     * refresh 토큰 저장하기
     */
    @Transactional
    public void UpdateRefreshToken(String id, String refreshToken){

        Member member = loginRepository.findLoginByIdAndSocial(id,"normal");
        if(member == null){ return; }

        member.updateRefreshToken(refreshToken);
    }

    /**
     * refresh 토큰으로 토큰 갱신하기
     */
    public Jwt refreshToken(String refreshToken){
        try{
            jwtProvider.getClaims(refreshToken);
            Member member = loginRepository.findByRefreshToken(refreshToken);
            if(member == null){ return null; }

            Map<String, Object> claims = new HashMap<>();
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(member.getId(),member.getName());
            String authenticatedUserJson = objectMapper.writeValueAsString(authenticatedUser);
            claims.put("AuthenticetedUser", authenticatedUserJson);
            Jwt jwt = jwtProvider.createJwt(claims);
            UpdateRefreshToken(member.getId(), jwt.getRefreshToken());
            return jwt;
        } catch (Exception e) {
            return null;
        }
    }


    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 일반 회원가입 시 아이디 중복 체크 메서드
     */
    private void ValidateDuplicateId(JoinRequestDto form) {
        Optional.ofNullable(loginRepository.findLoginByIdAndSocial(form.getId(), "normal"))
                .ifPresent(user -> {
                    throw new DuplicatedIDException(ErrorCode.DUPLICATED_USER_ID);
                });
    }

}
