package com.example.turnpage.domain.book.client;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookSearchClient extends BookClient{
    private final WebClient webClient;
    public BookSearchClient(WebClient.Builder webClientBuilder, @Value("${public-data.book-search.base-url}") String url) {
        this.webClient = webClientBuilder
                .baseUrl(url)
                .build();
    }

    public List<SaveBookRequest> getSearchResult(String keyword) {
        List<SaveBookRequest> books = new ArrayList<>();

            String json = fetchSearchResultJson(keyword);

            JSONArray items = jsonToItems(json);
            for (Object item : items) {
                books.add(itemToSaveBookRequest(item));
            }

        return books;
    }

    private String fetchSearchResultJson(String keyword) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("Query", keyword).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
