package project.booker.service.LibraryAPIService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.booker.controller.BookController.dto.LibraryInfo;
import project.booker.controller.BookController.dto.Librarys;

@Slf4j
@Service
@Transactional
public class LibraryAPIServiceImpl implements LibraryAPIService{

    @Value("${library.api.authKey}")
    private String authKey;

    /**
     * 도서 소장 도서관 조회
     */
    @Override
    public Librarys libSrchByBook(String isbn13, String region) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("authKey", authKey);
        params.add("isbn", isbn13);
        params.add("region", region);
        params.add("format", "json");

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://data4library.kr/api/libSrchByBook",
                HttpMethod.POST,
                request,
                String.class
        );

        log.info("response={}",response.getBody());

        Librarys librarys = new Librarys();
        try{
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode libs = jsonNode.get("response").get("libs");
            for (JsonNode lib : libs) {
                String libName = lib.get("lib").get("libName").asText();
                String address = lib.get("lib").get("address").asText();
                String tel = lib.get("lib").get("tel").asText();
                String operatingTime = lib.get("lib").get("operatingTime").asText();
                String closed = lib.get("lib").get("closed").asText();
                String latitude = lib.get("lib").get("latitude").asText();
                String longitude = lib.get("lib").get("longitude").asText();

                LibraryInfo libraryInfo = LibraryInfo.builder()
                        .libName(libName)
                        .address(address)
                        .tel(tel)
                        .operatingTime(operatingTime)
                        .closed(closed)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();

                librarys.getLibrarys().add(libraryInfo);
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return librarys;
    }
}
