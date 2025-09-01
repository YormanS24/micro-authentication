package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.exception.ResourceNotFoundException;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.util.LoggerGateway;
import co.com.pragma.model.util.TransactionalGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TransactionalGateway transactionalGateway;

    @Mock
    private LoggerGateway logger;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .userId(1L)
                .name("Yorman")
                .lastName("Jose")
                .birthDate(LocalDate.of(2001, 1, 24))
                .identityDocument("1007844444")
                .phone("3137644976")
                .email("sanchezyorman0@gmail.com")
                .address("Calle 45 #12-34")
                .baseSalary(new BigDecimal("2000000.00"))
                .build();

        when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createUser_success() {
        when(userRepository.existsEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.existsRoleId(user.getRoleId())).thenReturn(Mono.just(true));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.createUser(user))
                .verifyComplete();

        verify(userRepository).existsEmail(user.getEmail());
        verify(roleRepository).existsRoleId(user.getRoleId());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_conflict_emailAlreadyExists() {
        when(userRepository.existsEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(roleRepository.existsRoleId(user.getRoleId())).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.createUser(user))
                .expectError(ConflictException.class)
                .verify();

        verify(userRepository).existsEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_roleNotFound() {
        when(userRepository.existsEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.existsRoleId(user.getRoleId())).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.createUser(user))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(userRepository).existsEmail(user.getEmail());
        verify(roleRepository).existsRoleId(user.getRoleId());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserByDocumentNumber_shouldReturnUser_whenExists() {
        String documentNumber = "1007844444";
        when(userRepository.findUserByDocument(documentNumber)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.getUserByDocumentNumber(documentNumber))
                .expectNextMatches(foundUser ->
                        foundUser.getName().equals("Yorman") &&
                                foundUser.getEmail().equals("sanchezyorman0@gmail.com"))
                .verifyComplete();

        verify(userRepository).findUserByDocument(documentNumber);
    }

    @Test
    void getUserByDocumentNumber_shouldReturnError_whenNotExists() {
        String documentNumber = "999999";
        when(userRepository.findUserByDocument(documentNumber)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getUserByDocumentNumber(documentNumber))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(userRepository).findUserByDocument(documentNumber);
    }
}
