package chandanv.local.chandanv.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import chandanv.local.chandanv.resources.ApiResource;

@ControllerAdvice
public class GlobalExceptionHanler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResource<Map<String, String>> errorDetails = ApiResource.<Map<String, String>>builder()
            .success(false)
            .message("Có vấn đề xảy ra trong quá trình kiểm tra dữ liệu")
            .errors(errors)
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}