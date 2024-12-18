package com.springboot.backend.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/naver-map")
public class MapController {

    @Value("${naver.client-id}")
    private String NAVER_API_ID;

    @Value("${naver.secret}")
    private String NAVER_API_SECRET;


    // 네이버 검색 API를 이용하여 지정된 이름으로 장소를 검색

    // @param name 검색할 장소의 이름
    // @return 검색된 장소 목록
    @GetMapping("/place/{name}") // 경로에 이름을 포함하여 호출
    public List<Map<String, String>> naver(@PathVariable String name) { // 지정된 이름으로 장소 검색
        return searchPlace(name);  // "/api/naver-map/place/장소"와 같이 호출 진행
    }


    // 네이버 검색 API를 이용하여 동적으로 검색어를 지정하여 장소를 검색
    @GetMapping("/place") // 매개변수로 검색어를 동적으로 받아 장소 검색 진행
    public List<Map<String, String>> naverSearchDynamic(@RequestParam String query) { // 동적으로 검색어 지정
        return searchPlace(query); // "/api/naver-map/place?query=장소"와 같이 호출 진행
    }

    // 장소를 검색하는 메서드
    private List<Map<String, String>> searchPlace(String query) {
        List<Map<String, String>> places = new ArrayList<>();

        try {
            // UTF-8로 인코딩된 검색어 생성
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
            String encode = StandardCharsets.UTF_8.decode(buffer).toString();

            // 네이버 검색 API를 호출하기 위한 URI 생성
            URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com").path("/v1/search/local")
                    .queryParam("query", encode).queryParam("display", 10).queryParam("start", 1)
                    .queryParam("sort", "random").encode().build().toUri();

            // RestTemplate을 사용하여 네이버 API에 요청을 보냄
            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> req = RequestEntity.get(uri).header("X-Naver-Client-Id", NAVER_API_ID)
                    .header("X-Naver-Client-Secret", NAVER_API_SECRET).build();

            // API 응답 데이터를 JSON 형식으로 변환
            ResponseEntity<String> response = restTemplate.exchange(req, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // 검색 결과 중에서 장소 정보를 추출하여 리스트에 저장
            JsonNode itemsNode = rootNode.path("items");
            for (JsonNode itemNode : itemsNode) {
                Map<String, String> place = new HashMap<>();
                place.put("title", itemNode.path("title").asText()); // 장소 이름
                place.put("address", itemNode.path("address").asText()); // 장소 주소
                /*
                 * restaurant.put("mapx", itemNode.path("mapx").asText());
                 * restaurant.put("mapy", itemNode.path("mapy").asText());
                 */
                // 위도와 경도를 double 형식으로 변환하여 저장
                double latitude = Double.parseDouble(itemNode.path("mapy").asText()) / 1e7; // 위도
                double longitude = Double.parseDouble(itemNode.path("mapx").asText()) / 1e7; // 경도
                place.put("latitude", String.valueOf(latitude));
                place.put("longitude", String.valueOf(longitude));

                places.add(place); // 리스트에 추가
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return places;
    }


}
