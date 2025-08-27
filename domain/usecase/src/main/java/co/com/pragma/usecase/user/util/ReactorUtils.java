package co.com.pragma.usecase.user.util;

import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class ReactorUtils {

    public static <T> Mono<T> checkTrueOrElse(Mono<Boolean> condition, Supplier<? extends Throwable> exceptionSupplier) {
        return condition.flatMap(aBoolean -> Boolean.TRUE.equals(aBoolean) ? Mono.error(exceptionSupplier.get()) : Mono.empty());
    }
}
