package hello.login.domain.controller.api;

import hello.login.domain.dto.ResponseDto;
import hello.login.domain.dto.UserDto;
import hello.login.domain.model.User;
import hello.login.domain.repository.UserRepository;
import hello.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> join(@RequestBody UserDto userDto) {
        log.info("회원가입 메서드 호출: " + userDto);
        userService.join(userDto);

        // 회원가입 후 자동 로그인 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    /**
     * 회원수정
     */
    @PutMapping("/user/update")
    public ResponseDto<Integer> update(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);

        // 세션값 변경
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    /**
     * 회원정보
     */
    @GetMapping("/user")
    public ResponseDto<UserDto> getUserInfo(@RequestParam String username) {
        UserDto userDto = userService.getUserInfo(username);
        return new ResponseDto<>(HttpStatus.OK.value(), userDto);
    }
}
