package org.example.dto.ExceptionDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MethodArgumentNotValidExceptionResponseDTO {


    private LocalDateTime time;
    private String path;
    private int statusCode;
    private String reasonPhrase;
    private Map<String, String> messageMap;


}
