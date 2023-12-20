package project.booker.service.joinService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.domain.Member;
import project.booker.dto.joinController.request.JoinRequestDto;
import project.booker.exception.exceptions.DuplicatedIDException;
import project.booker.repository.JoinRepository;
import project.booker.exception.errorcode.ErrorCode;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinServiceImpl implements JoinService {

    private final JoinRepository joinRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
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

        joinRepository.save(member);
    }


    //--------------------------------------Private Method-----------------------------------------------------

    /*
     * 일반 회원가입 시 아이디 중복 체크 메서드
     */
    private void ValidateDuplicateId(JoinRequestDto form) {
        Optional.ofNullable(joinRepository.findJoinByIdAndSocial(form.getId(), "normal"))
                .ifPresent(user -> {
                    throw new DuplicatedIDException(ErrorCode.DUPLICATED_USER_ID);
                });
    }

}
