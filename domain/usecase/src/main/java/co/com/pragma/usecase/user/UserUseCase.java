package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.util.ReactorUtils;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.user.util.MessageUseCaseConstant.EMAIL_ALREADY_EXISTS;

public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Void> createUser(User user) {
        return ReactorUtils.checkTrueOrElse(userRepository.existsEmail(user.getEmail()), () -> new ConflictException(EMAIL_ALREADY_EXISTS))
                .then(userRepository.save(user))
                .then();
    }
}
