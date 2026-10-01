package org.example.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.ExceptionDTOs.ExceptionResponseDTO;
import org.example.dto.ExceptionDTOs.MethodArgumentNotValidExceptionResponseDTO;
import org.example.exception.ConflictExcpetions.children.*;
import org.example.exception.ConflictExcpetions.parent.ConflictException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDTO);
    }


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataAccessException(DataAccessException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An internal database error occurred."
        );


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponseDTO);
    }


    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<ExceptionResponseDTO> handleConcurrencyFailureException(ConcurrencyFailureException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "The operation could not be completed due to a concurrent modification."
        );


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponseDTO);
    }






    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ExceptionResponseDTO> handleInvalidDateRangeException(InvalidDateRangeException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDTO);

    }




    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDTO);
    }



    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponseDTO> handleConflictException(ConflictException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );


        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDTO);
    }





    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest){

        Map<String,String> map = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> map.put(fieldError.getField(),fieldError.getDefaultMessage()));

        MethodArgumentNotValidExceptionResponseDTO methodArgumentNotValidExceptionResponseDTO = new MethodArgumentNotValidExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                map
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(methodArgumentNotValidExceptionResponseDTO);

    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleGenericException(Exception ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage()
                );



        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponseDTO);

    }
}
