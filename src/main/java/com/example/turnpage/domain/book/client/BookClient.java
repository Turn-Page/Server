package com.example.turnpage.domain.book.client;

import com.example.turnpage.domain.book.dto.BookRequest;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.code.BookErrorCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Optional;

public class BookClient {

    /*
    json을 items 배열로 변환
     */
    JSONArray jsonToItems(String json) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(json);
        }
        catch (ParseException e) {
            throw new BusinessException(BookErrorCode.BOOK_JSON_PARSE_ERROR);
        }

        return (JSONArray) jsonObject.get("item");

    }

    /*
    items 배열을 SaveBookRequest dto로 변환
     */
    BookRequest.SaveBookRequest itemToSaveBookRequest(Object objectItem) {
        JSONObject item = (JSONObject) objectItem;
        return BookRequest.SaveBookRequest.builder()
                .itemId(Long.parseLong(item.get("itemId").toString()))
                .title(item.get("title").toString())
                .author(item.get("author").toString())
                .cover(changeCoverImageSize(item.get("cover").toString()))
                .isbn(item.get("isbn13").toString())
                .publisher(item.get("publisher").toString())
                .publicationDate(item.get("pubDate").toString())
                .description(item.get("description").toString())
                .rank(checkRank(Optional.ofNullable(item.get("rank"))))
                .build();

    }

    private Integer checkRank(Optional rank) {
        return (Integer) rank.orElse(null);
    }

    private String changeCoverImageSize(String cover) {
        return cover.replace("coversum", "cover500");
    }


}
