package yumyum.demo.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final Map<String, String> errorFieldMessage = new HashMap<>();

    public void putErrorMessage(String field, String message) {
        errorFieldMessage.put(field, message);
    }
}
