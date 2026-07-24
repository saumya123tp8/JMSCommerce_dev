package com.example.JMSCommerce.Utility;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private String error;
    private LocalDateTime timestamp;
    private String path;
    public static <T> ApiResponse<T> success(T data, String message){
      return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

    }

    public static <T> ApiResponse<T> error(String error, String message, String path){
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();

    }


}
