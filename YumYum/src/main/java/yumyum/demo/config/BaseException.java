package yumyum.demo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 재정의한 예외로, 애플리케이션에서 예상된 예외를 처리할 때 사용한다.
 */

@Getter
@Setter
public class BaseException extends RuntimeException {
    private BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}