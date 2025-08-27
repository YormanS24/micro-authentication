package co.com.pragma.r2dbc.user.repository;

import co.com.pragma.r2dbc.user.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<Boolean> existsByEmailIgnoreCase(String email);
}
