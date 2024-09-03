package com.example.turnpage.global.utils;

import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.result.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class HandlerUtils {
    public static void writeResponse(HttpServletRequest request, HttpServletResponse response,
                                     ResultResponse resultResponse) throws IOException {
        response.setStatus(resultResponse.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, resultResponse);
            os.flush();
        }
    }

    public static void writeErrorResponse(HttpServletResponse response, BusinessException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final Map<String, Object> body = new LinkedHashMap<>();

        body.put("code", exception.getErrorCode().getCode());
        body.put("status", exception.getErrorCode().getStatus());
        body.put("message",exception.getErrorCode().getMessage());

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, body);
            os.flush();
        }
    }
}
