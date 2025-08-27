package co.com.pragma.api.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationJakartaException extends RuntimeException {

    private final Errors errors;

    public ValidationJakartaException(Errors errors) {
        super("Error de validación");
        this.errors = errors;
    }
}
