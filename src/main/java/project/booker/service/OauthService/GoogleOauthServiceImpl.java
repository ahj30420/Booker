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
import org.springframework.web.client.RestTemplate;
import project.booker.domain.Member;
import project.booker.domain.Enum.Social;
import project.booker.controller.OauthController.dto.GoogleTokens;
import project.booker.controller.OauthController.dto.GoogleUserInfo;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.CodeException;
import project.booker.exception.exceptions.InvalidAccessToken;
import project.booker.repository.LoginRepository;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GoogleOauthServiceImpl implements GoogleOauthService{

    @Value("${google.client_id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${goole.client_secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${goole.redirect_url}")
    private String GOOGLE_REDIRECT_URL;

    private final LoginRepository loginRepository;

    /**
     * 구글 Token 발급
     * 1. Code + Clien_id + Client_Secret + Redirect_uri를 조합하여 Token 발급 요청을 보낸다
     * 2. 받은 응답 값에 포함된 AccessToken, RefreshTokne 값을 GoogleTokens 객체에 저장 후 반환
     */
    @Override
    public GoogleTokens getGoogleToken(String code) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_REDIRECT_URL);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params,httpHeaders);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                request,
                String.class
        );

        GoogleTokens googleTokens = null;

        try{
            googleTokens = objectMapper.readValue(accessTokenResponse.getBody(), GoogleTokens.class);
        } catch (Exception e) {
            throw new CodeException(ErrorCode.INVALID_CODE);
        }

        return googleTokens;
    }

    /**
     * AccessToken으로 회원 정보 조회
     * 1. 구글 토큰 중 AccessToken으로 정보 조회 요청
     * 2. 받은 회원 정보 응답에서 필요한 정보만 GoogleUserInfo 객체에 저장 후 반환 (Id, 이메일, 이름)
     */
    @Override
    public GoogleUserInfo getGoogleInfo(GoogleTokens googleTokens) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer" + googleTokens.getAccess_token());

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v1/userinfo",
                HttpMethod.GET,
                request,
                String.class
        );

        JsonNode jsonNode = null;
        String id = null;
        String email = null;
        String name = null;

        try{
            jsonNode = objectMapper.readTree(response.getBody());
            id = jsonNode.get("id").asText();
            email = jsonNode.get("email").asText();
            name = jsonNode.get("name").asText();
        } catch (Exception e){
            throw new InvalidAccessToken(ErrorCode.INVALID_ACCESSTOKEN);
        }

        GoogleUserInfo googleUserInfo = new GoogleUserInfo(id,email,name);

        return googleUserInfo;
    }

    /**
     * 구글에서 조회한 회원 정보를 바탕으로 회원가입
     * 1. 구글 Id 정보로 회원 조회 (기존 회원인지 신규 회원인지 파악)
     * 2. 신규 회원이라면 회원가입 후 회원정보 + "신규회원 O" 반환
     * 3. 기존 회원이라면 회원정보 + "신규회원 X" 반환 (회원 가입 X)
     */
    @Override
    public Map<String, Object> GoogleJoin(GoogleUserInfo googleUserInfo) {
        Member member = loginRepository.findMemberIdByIdAndSocial(googleUserInfo.getId(), Social.GOOGLE);

        Map<String, Object> result = new HashMap<>();
        boolean isNewMember = false;

        if(member == null){
            isNewMember = true;
            String id = googleUserInfo.getId();
            String email = googleUserInfo.getEmail();
            String name = googleUserInfo.getName();
            String pw = generateRandomString(20);
            LocalDate redate = LocalDate.now();
            Member NewGoogleMember = Member.createMember(id,pw,name,email,null, Social.GOOGLE,redate);
            loginRepository.save(NewGoogleMember);

            result.put("member", NewGoogleMember);
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
