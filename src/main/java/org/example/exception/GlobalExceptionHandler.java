package org.example.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.ExceptionDTOs.ExceptionResponseDTO;
import org.example.dto.ExceptionDTOs.MethodArgumentNotValidExceptionResponseDTO;
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


    @ExceptionHandler(TimePeriodAlreadyCoveredException.class)
    public ResponseEntity<ExceptionResponseDTO> handleTimePeriodAlreadyCoveredException(TimePeriodAlreadyCoveredException ex, HttpServletRequest httpServletRequest){

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                LocalDateTime.now(),
                httpServletRequest.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDTO);

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



    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest httpServletRequest){

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
