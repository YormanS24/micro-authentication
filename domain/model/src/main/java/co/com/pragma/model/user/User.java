package co.com.pragma.model.user;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String nit;

    private String phone;

    private String email;

    private String address;
}
