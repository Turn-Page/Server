package com.example.turnpage.domain.book.client;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.BookErrorCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class BestSellerClient {
    private final WebClient webClient;

    public BestSellerClient(WebClient.Builder webClientBuilder, @Value("${public-data.bestseller.base-url}") String url) {
        this.webClient = webClientBuilder
                .baseUrl(url)
                .build();
    }

    public List<SaveBookRequest> getBestSellerBooks() {
        List<SaveBookRequest> books = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            int start = i;
            String json = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("start", start)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONArray items = jsonToItems(json);
            for (Object item : items) {
                books.add(itemToSaveBookRequest(item));
            }
        }

        return books;
    }

    /*
    json을 items 배열로 변환
     */
    private JSONArray jsonToItems(String json) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(json);
        }
        catch (ParseException e) {
            throw new BusinessException(BookErrorCode.BEST_SELLER_JSON_PARSE_ERROR);
        }

        return (JSONArray) jsonObject.get("item");

    }

    /*
    items 배열을 SaveBookRequest dto로 변환
     */
    private SaveBookRequest itemToSaveBookRequest (Object objectItem) {
        JSONObject item = (JSONObject) objectItem;
        return SaveBookRequest.builder()
                .itemId(Long.parseLong(item.get("itemId").toString()))
                .title(item.get("title").toString())
                .author(item.get("author").toString())
                .cover(item.get("cover").toString())
                .isbn(item.get("isbn13").toString())
                .publisher(item.get("publisher").toString())
                .publicationDate(item.get("pubDate").toString())
                .description(item.get("description").toString())
                .rank(Integer.parseInt((item.get("bestRank")).toString()))
                .build();

    }

}
