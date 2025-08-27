package co.com.pragma.api.exception;

import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @NonNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();

        return switch (ex) {
            case ValidationJakartaException exception -> writeJson(
                    response,
                    HttpStatus.BAD_REQUEST,
                    exception.getErrors().getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList()
            );
            case ConflictException exception ->
                    writeJson(response, HttpStatus.CONFLICT, Map.of("message", exception.getMessage()));
            case ResourceNotFoundException exception ->
                    writeJson(response, HttpStatus.NOT_FOUND, Map.of("message", exception.getMessage()));
            default -> Mono.error(ex);
        };
    }

    private Mono<Void> writeJson(ServerHttpResponse response, HttpStatus status, Object body) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            var buffer = response.bufferFactory().wrap(jsonBody.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
