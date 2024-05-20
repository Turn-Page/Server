package com.example.turnpage.global.utils;

import com.example.turnpage.global.error.ErrorCode;
import com.example.turnpage.global.error.ErrorResponse;
import com.example.turnpage.global.result.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public class HandlerUtils {
    public static void writeResponse(HttpServletRequest request, HttpServletResponse response,
                                     ResultResponse resultResponse) throws IOException, ServletException {
        response.setStatus(resultResponse.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, resultResponse);
            os.flush();
        }
    }
}
