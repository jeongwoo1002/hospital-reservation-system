package hello.login.domain.service;

import hello.login.domain.dto.UserDto;
import hello.login.domain.model.User;
import hello.login.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public void join(UserDto userDto) {
        User user = User.join(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    /**
     * 회원수정 - 비밀번호 및 기타 정보
     */
    @Transactional
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 수정 실패"));

        // 비밀번호가 제공된 경우 암호화하여 업데이트
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encPassword);
        }

        // 나머지 필드 업데이트
        user.setName(userDto.getName());
        user.setNumber(userDto.getNumber());
        user.setAddress(userDto.getAddress());
        user.setResident(userDto.getResident());

        // 사용자 정보를 데이터베이스에 저장 (JPA가 자동으로 처리)
        userRepository.save(user); // 이 줄은 선택적입니다. JPA는 @Transactional을 통해 자동으로 저장합니다.
    }

    /**
     * 사용자 정보 조회
     */
    public UserDto getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getNumber(), user.getAddress(), user.getResident());
    }

    /**
     * 사용자 이름으로 사용자 찾기
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }
}
