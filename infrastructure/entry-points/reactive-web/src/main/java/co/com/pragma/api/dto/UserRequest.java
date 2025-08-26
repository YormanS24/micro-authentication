package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.api.util.MessageConstants.*;

public record UserRequest(
        @NotBlank(message = VALID_NAME)
        @Size(min = 3, max = 50, message = VALID_NAME_SIZE)
        @JsonProperty(value = "name")
        String name,

        @NotBlank(message = VALID_LAST_NAME)
        @Size(min = 3, max = 50, message = VALID_LAST_NAME_SIZE)
        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("birth_date")
        LocalDate birthDate,

        @NotBlank(message = VALID_DOCUMENT)
        @Size(max = 20, message = VALID_DOCUMENT_SIZE)
        @JsonProperty(value = "identity_document")
        String identityDocument,

        @Size(max = 20, message = VALID_PHONE_SIZE)
        String phone,

        @NotBlank(message = VALID_EMAIL)
        @Email(message = VALID_EMAIL_FORMAT)
        @Size(min = 1, max = 100, message = VALID_EMAIL_SIZE)
        String email,

        @Size(max = 100, message = VALID_ADDRESS_SIZE)
        String address,

        @NotNull(message = VALID_ROLE)
        @Positive(message = VALID_ROLE_NUMBER)
        @JsonProperty("role_id")
        Long roleId,

        @NotNull(message = VALID_BASE_SALARY)
        @DecimalMin(value = BASE_SALARY_MIN, message = VALID_BASE_SALARY_MIN)
        @DecimalMax(value = BASE_SALARY_MAX, message = VALID_BASE_SALARY_MAX)
        @JsonProperty("base_salary")
        BigDecimal baseSalary
) {
}
