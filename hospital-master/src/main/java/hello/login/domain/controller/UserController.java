package hello.login.domain.controller;

import hello.login.domain.config.auth.PrincipalDetail;
import hello.login.domain.dto.LoginForm;
import hello.login.domain.dto.UserDto;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.UserRepository;
import hello.login.domain.service.ReservationService;
import hello.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ReservationService reservationService;
//    private final UserRepository userRepository;

    /**
     * 회원가입 폼으로 이동
     */
    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    /**
     * 로그인 폼으로 이동
     */
    @GetMapping("/auth/loginForm")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "user/loginForm";
    }

    /**
     * 회원수정 폼
     */
    @GetMapping("/user/updateForm")
    public String updateForm(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
        UserDto userDto = userService.getUserInfo(principal.getUsername());
        model.addAttribute("user", userDto);
        return "user/updateForm";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userDto);
            redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 수정에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/user/info"; // 수정 후 이동할 페이지
    }

    /**
     * 회원 정보 조회
     */
    @GetMapping("/user/userInfo")
    public String getUserInfo(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        UserDto userDto = userService.getUserInfo(username);
        model.addAttribute("user", userDto);
        return "user/userInfo";
    }
    @GetMapping("/user/reservations")
    public String getUserReservations(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
        Long userId = principal.getUser().getId(); // 로그인한 사용자의 ID 가져오기
        List<Reservation> reservations = reservationService.getUserReservations(userId);
        model.addAttribute("reservations", reservations);
        return "reservation/userReservations"; // 반환할 HTML 경로
    }

}
