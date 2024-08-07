package com.example.turnpage.domain.book.client;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.code.BookErrorCode;
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
public class BestSellerClient extends BookClient {
    private final WebClient webClient;

    public BestSellerClient(WebClient.Builder webClientBuilder, @Value("${public-data.bestseller.base-url}") String url) {
        this.webClient = webClientBuilder
                .baseUrl(url)
                .build();
    }

    public List<SaveBookRequest> getBestSellerBooks() {
        List<SaveBookRequest> books = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            String json = fetchBestSellerJson(i);

            JSONArray items = jsonToItems(json);
            for (Object item : items) {
                books.add(itemToSaveBookRequest(item));
            }
        }

        return books;
    }

    private String fetchBestSellerJson(int start) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("start", start).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
