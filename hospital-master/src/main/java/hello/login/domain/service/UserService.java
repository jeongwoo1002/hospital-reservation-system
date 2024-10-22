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
        try {
            User user = User.join(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("회원가입 오류: ", e);
            throw e; // 예외를 다시 던져서 호출자에게 알림
        }
    }

    /**
     * 회원수정 - 비밀번호 및 기타 정보
     */
    @Transactional
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 수정 실패"));

        // 비밀번호가 제공된 경우에만 업데이트, 그렇지 않으면 기존 값 유지
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encPassword);
        }

        // 이름이 빈 값이 아니면 업데이트
        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            user.setName(userDto.getName());
        }

        // 번호가 빈 값이 아니면 업데이트
        if (userDto.getNumber() != null && !userDto.getNumber().isEmpty()) {
            user.setNumber(userDto.getNumber());
        }

        // 주소가 빈 값이 아니면 업데이트
        if (userDto.getAddress() != null && !userDto.getAddress().isEmpty()) {
            user.setAddress(userDto.getAddress());
        }

        // 주민번호가 빈 값이 아니면 업데이트
        if (userDto.getResident() != null && !userDto.getResident().isEmpty()) {
            user.setResident(userDto.getResident());
        }

        // 사용자 정보를 데이터베이스에 저장
        userRepository.save(user);
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
