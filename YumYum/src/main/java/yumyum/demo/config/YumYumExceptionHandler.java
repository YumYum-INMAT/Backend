package yumyum.demo.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.ArrayList;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class YumYumExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseResponseStatus> handleBaseException(BaseException e) {
        log.warn("BaseException has occured. this is business exception. message: [{}]", e.getMessage());
        return new BaseResponse(e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorCodes = new ArrayList<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            errorCodes.add(fieldError.getDefaultMessage());
        }
        log.warn("MethodArgumentNotValidException has occured. please check request body. message: [{}]", e.getMessage());
        return new BaseResponse<>(errorCodes, BaseResponseStatus.REQUEST_ERROR);
    }

    @ExceptionHandler({BindException.class, HttpMessageNotReadableException.class, InvalidFormatException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class, ValidationException.class})
    public BaseResponse<BaseResponseStatus> handleRequestException(Exception e) {
        log.warn("{} has occured. please check request value. message: [{}]", e.getClass().getSimpleName(), e.getMessage());
        if (e.getMessage().contains("LocalDate")) {
            return new BaseResponse<>(BaseResponseStatus.WRONG_DATE);
        }
        return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<BaseResponseStatus> handleException(Exception e) {
        log.error("Exception has occured.", e);
        return new BaseResponse<>(BaseResponseStatus.UNEXPECTED_ERROR);
    }
}
