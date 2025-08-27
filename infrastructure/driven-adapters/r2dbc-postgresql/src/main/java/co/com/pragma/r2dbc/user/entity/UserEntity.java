package co.com.pragma.r2dbc.user.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @Column("user_id")
    private Long userId;

    @Column("name")
    private String name;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("identity_document")
    private String identityDocument;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("address")
    private String address;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("role_id")
    private Long roleId;
}
