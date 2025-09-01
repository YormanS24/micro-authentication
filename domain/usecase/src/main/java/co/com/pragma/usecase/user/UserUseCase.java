package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.exception.ResourceNotFoundException;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.util.LoggerGateway;
import co.com.pragma.model.util.TransactionalGateway;
import co.com.pragma.usecase.user.util.ReactorUtils;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.user.util.MessageUseCaseConstant.EMAIL_ALREADY_EXISTS;
import static co.com.pragma.model.user.util.MessageUseCaseConstant.ROLE_NOT_FOUND;

public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoggerGateway loggerGateway;
    private final TransactionalGateway transactionalGateway;

    public UserUseCase(UserRepository userRepository, RoleRepository roleRepository, LoggerGateway loggerGateway, TransactionalGateway transactionalGateway) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.loggerGateway = loggerGateway;
        this.transactionalGateway = transactionalGateway;
    }

    public Mono<Void> createUser(User user) {
        loggerGateway.info("Nuevo usuario a registrar con cc: [{}]", user);

        return transactionalGateway.executeInTransaction(
                ReactorUtils.checkTrueOrElse(userRepository.existsEmail(user.getEmail()), () -> new ConflictException(EMAIL_ALREADY_EXISTS))
                        .then(ReactorUtils.checkTrueOrElse(roleRepository.existsRoleId(user.getRoleId()).map(aBoolean -> !aBoolean), () -> new ResourceNotFoundException(ROLE_NOT_FOUND)))
                        .then(userRepository.save(user))
                        .then()
        );
    }
}
