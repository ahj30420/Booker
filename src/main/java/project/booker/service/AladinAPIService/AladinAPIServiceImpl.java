package project.booker.service.AladinAPIService;

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
import project.booker.controller.BookController.dto.BestSeller;
import project.booker.controller.BookController.dto.BestSellerList;
import project.booker.controller.BookController.dto.BookInfo;

import java.time.LocalDate;
import java.util.ArrayList;


@Slf4j
@Service
@Transactional
public class AladinAPIServiceImpl implements AladinAPIService{

    @Value("${aladin.api.ttbkey}")
    private String TTBKey;

    /**
     * 베스트 셀러 조회
     * params
     * 1. QueryType: 리스트 종류 (ItemNewAll, ItemNewSpecial, ItemEditorChoice, BestSeller, BlogBest)
     * 2. MaxResults: 검색결과 한 페이당 최대 출력 개수
     * 3. Start: 검색결과 시작 페이지
     * 4. SearchTarget: 조회할 목록 종류 (Book, Music, DVD)
     * 5. output: 출력방법 (XML, JSON)
     * 6. Version: 검색 API 버전 (최신버전 20131101)
     */
    @Override
    public BestSellerList getBestSeller(String start) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("ttbkey", TTBKey);
        params.add("QueryType", "Bestseller");
        params.add("MaxResults", "5");
        params.add("start", start);
        params.add("SearchTarget", "Book");
        params.add("output", "JS");
        params.add("Version", "20131101");

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://www.aladin.co.kr/ttb/api/ItemList.aspx",
                HttpMethod.POST,
                request,
                String.class
        );

        ArrayList<BestSeller> bestSellers = new ArrayList<>();

        try{
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode items = jsonNode.get("item");
            for(JsonNode item: items){
                String isbn13 = item.get("isbn13").asText();
                String title = item.get("title").asText();
                String cover = item.get("cover").asText();
                String author = item.get("author").asText();
                String publisher = item.get("publisher").asText();
                String description = item.get("description").asText();
                String category = item.get("categoryName").asText();

                BestSeller bestSeller = new BestSeller(isbn13, title, cover, author, publisher, category, description);
                bestSellers.add(bestSeller);
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        BestSellerList bestSellerList = new BestSellerList(start, bestSellers);
        return bestSellerList;
    }

    /**
     * 특정 도서 정보 조회
     * params
     * 1. itemIdType: 조회용 파라미터 종류 (ISBN, ISBN13, ITEMID) //가급적 13자리 ISBN으로 조회 권장!
     * 2. ItemId: 상품을 구분짓는 유일한 값 (ISBN, ISBN13, ITEMID)
     * 3. output: 출력방법 (XML, JSON)
     * 4. Version: 검색 API 버전 (최신버전 20131101)
     */
    @Override
    public BookInfo BookLookUp(String ISBN13) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
        params.add("ttbkey", TTBKey);
        params.add("itemIdType", "ISBN13");
        params.add("ItemId", ISBN13);
        params.add("output", "JS");
        params.add("Version", "20131101");

        HttpEntity<MultiValueMap> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
            "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx",
                HttpMethod.POST,
                request,
                String.class
        );

        BookInfo bookInfo;

        try{
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode item = jsonNode.get("item").get(0);

            String isbn13 = item.get("isbn13").asText();
            String title = item.get("title").asText();
            String author = item.get("author").asText();
            String publisher = item.get("publisher").asText();
            LocalDate pubDate = LocalDate.parse(item.get("pubDate").asText());
            String cover = item.get("cover").asText();
            String category = item.get("categoryName").asText();
            String description = item.get("description").asText();

            bookInfo = new BookInfo(isbn13, title, author, publisher, pubDate, cover, category, description);

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return bookInfo;
    }
}
