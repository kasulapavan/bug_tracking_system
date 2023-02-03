package net.thrymr.task.custom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLTimeoutException;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse> handleExceptions(
            Exception ex,
            WebRequest request
    )
    {
        if(ex instanceof RuntimeException) {
            return composeTimeOutException(ex, request);
        }else if(ex instanceof SQLTimeoutException){
            return composeRunTimeException(ex, request);
        }else{
            return composeGenericException(ex, request);
        }
    }

    private  ResponseEntity<ApiResponse> composeTimeOutException( Exception ex,
                                                                             WebRequest request){
//Write if any logic required for a specific  Exception type as per business logic
        return new ResponseEntity<>(composeAppResponseDTO(ex.getMessage(),502, request.getDescription(true)), HttpStatus.GATEWAY_TIMEOUT);
    }

    private  ResponseEntity<ApiResponse> composeRunTimeException( Exception ex,
                                                                             WebRequest request){
//Write if any logic required for a specific  Exception type as per business logic
        return new ResponseEntity<>(composeAppResponseDTO(ex.getMessage(),500, request.getDescription(true)), HttpStatus.BAD_REQUEST);
    }

    private  ResponseEntity<ApiResponse> composeGenericException( Exception ex,
                                                                             WebRequest request){
//Write if any logic required for a specific  Exception type as per business logic
        return new ResponseEntity<>(composeAppResponseDTO(ex.getMessage(),403, request.getDescription(true)), HttpStatus.UNAUTHORIZED);
    }
    private ApiResponse composeAppResponseDTO(String message, int errorCode,Object payload ){
        return new ApiResponse( errorCode, message, payload);
    }



}
