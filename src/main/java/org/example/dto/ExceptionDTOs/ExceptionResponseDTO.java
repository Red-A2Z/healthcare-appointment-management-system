package org.example.dto.ExceptionDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionResponseDTO {


    private LocalDateTime time;
    private String path;
    private int statusCode;
    private String reasonPhrase;
    private String message;



}
