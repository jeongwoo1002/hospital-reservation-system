package hello.login.domain.dto;

import hello.login.domain.model.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "아이디를 입력해 주세요")
    @Size(min = 2, max = 6, message = "아이디는 2~6자 사이로 입력 해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password;

    @NotBlank(message = "이름을 입력해 주세요")
    private String name;

    @NotBlank(message = "전화번호를 입력해 주세요")
    private String number;

    @NotBlank(message = "주소를 입력해 주세요")
    private String address;

    @NotBlank(message = "주민번호를 입력해 주세요")
    private String resident;

    private Role role;


    // Getters and setters


    public UserDto(Long id, String username, String name, String number, String address, String resident) {
        this.id = id;
        this.username = username;
//        this.password = password;
        this.name = name;
        this.number = number;
        this.address = address;
        this.resident = resident;
    }
}

