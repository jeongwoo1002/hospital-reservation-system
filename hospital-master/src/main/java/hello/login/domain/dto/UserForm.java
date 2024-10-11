package hello.login.domain.dto;

import javax.validation.constraints.NotBlank;

public class UserForm {
    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "전화번호를 입력해 주세요")
    private String number;

    @NotBlank(message = "주소를 입력해 주세요")
    private String address;

    @NotBlank(message = "주민번호를 입력해 주세요")
    private String resident;
}
