package hello.login.domain.model;

import hello.login.domain.dto.UserDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 13)
    private String number;

    @Column(nullable = false, length = 14)
    private String resident;

    @Column(nullable = false, length = 100)
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    private Timestamp createDate;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    //==회원가입==//
    public static User join(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .number(userDto.getNumber())
                .resident(userDto.getResident())
                .address(userDto.getAddress())
                .role(Role.USER)
                .build();
    }

    //==사용자 수정==//
    public void updateUser(String password) {
        this.password = password;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateNumber(String number) {
        this.number = number;
    }
}
