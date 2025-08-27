package co.com.pragma.api.util;

import co.com.pragma.api.exception.ValidationJakartaException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;


@Component
@AllArgsConstructor
public class ValidationUtils {

    private final SmartValidator smartValidator;

    public <T> Mono<T> validateBody(ServerRequest request, Class<T> clazz) {
        return request.bodyToMono(clazz)
                .flatMap(body -> {
                            var errors = new BeanPropertyBindingResult(body, clazz.getName());
                            smartValidator.validate(body, errors);

                            if (errors.hasErrors()) {
                                return Mono.error(new ValidationJakartaException(errors));
                            }

                            return Mono.just(body);
                        }
                );
    }
}
