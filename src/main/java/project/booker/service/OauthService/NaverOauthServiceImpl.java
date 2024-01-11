package project.booker.service.OauthService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project.booker.domain.Member;
import project.booker.domain.Enum.Social;
import project.booker.dto.NaverTokens;
import project.booker.dto.NaverUserInfo;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.CodeException;
import project.booker.exception.exceptions.InvalidAccessToken;
import project.booker.repository.LoginRepository;
import project.booker.util.jwt.JwtProvider;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NaverOauthServiceImpl implements NaverOauthService {

    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    private final LoginRepository loginRepository;
    private final JwtProvider jwtProvider;


    /**
     * 네이버 Token 발급
     * 1. Code + Clien_id + Client_Secret + state를 조합하여 Token 발급 요청을 보낸다
     * 2. 받은 응답 값에 포함된 AccessToken, RefreshTokne 값을 NaverTokens 객체에 저장 후 반환
     */
    @Override
    public NaverTokens getNaverToken(String code, String state) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                request,
                String.class
        );

        NaverTokens naverTokens = null;
        try{
            naverTokens = mapper.readValue(accessTokenResponse.getBody(), NaverTokens.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CodeException(ErrorCode.INVALID_CODE);
        }

        return naverTokens;
    }

    /**
     * AccessToken으로 회원 정보 조회
     * 1. 네이버 토큰 중 AccessToken으로 정보 조회 요청
     * 2. 받은 회원 정보 응답에서 필요한 정보만 NaverUserInfo 객체에 저장 후 반환 (Id, 이메일, 이름)
     */
    @Override
    public NaverUserInfo getNaverInfo(NaverTokens naverTokens) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + naverTokens.getAccess_token());

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                request,
                String.class
        );

        log.info("response={}",response.getBody());

        JsonNode jsonNode = null;
        try {
             jsonNode = mapper.readTree(response.getBody());
        } catch (Exception e){
            throw new InvalidAccessToken(ErrorCode.INVALID_ACCESSTOKEN);
        }

        String id = jsonNode.get("response").get("id").asText();
        String email = jsonNode.get("response").get("email").asText();
        String name = jsonNode.get("response").get("name").asText();

        NaverUserInfo naverUserInfo = new NaverUserInfo(id,email,name);

        return naverUserInfo;
    }

    /**
     * 네이버에서 조회한 회원 정보를 바탕으로 회원가입
     * 1. 네이버 Id 정보로 회원 조회 (기존 회원인지 신규 회원인지 파악)
     * 2. 신규 회원이라면 회원가입 후 회원정보 + "신규회원 O" 반환
     * 3. 기존 회원이라면 회원정보 + "신규회원 X" 반환 (회원 가입 X)
     */
    @Override
    public Map<String, Object> NaverJoin(NaverUserInfo naverUserInfo) {
        Member member = loginRepository.findMemberIdByIdAndSocial(naverUserInfo.getId(), Social.NAVER);

        Map<String, Object> result = new HashMap<>();
        boolean isNewMember = false;

        if(member == null){
            isNewMember = true;
            String id =  naverUserInfo.getId();
            String email = naverUserInfo.getEmail();
            String name = naverUserInfo.getName();
            String pw = generateRandomString(20);
            LocalDate redate = LocalDate.now();
            Member NewNaverMember = Member.createMember(id,pw,name,email,null, Social.NAVER, redate);
            loginRepository.save(NewNaverMember);

            result.put("member", NewNaverMember);
            result.put("isNewMember", isNewMember);

            return result;
        } else if(member.getMemberProfile() == null){
            isNewMember = true;

            result.put("member", member);
            result.put("isNewMember", isNewMember);

            return result;
        }

        result.put("member", member);
        result.put("isNewMember", isNewMember);

        return result;
    }

    //--------------------------------------Private Method-----------------------------------------------------

    // 무작위 문자열 생성
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+="; // 사용할 문자들
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        // 지정된 길이만큼 반복하여 무작위 문자열 생성
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

}
