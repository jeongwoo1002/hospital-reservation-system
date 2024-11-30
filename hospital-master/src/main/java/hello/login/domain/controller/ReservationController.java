package hello.login.domain.controller;

import hello.login.domain.config.auth.PrincipalDetail;
import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Ophthalmology;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.OphthalmologyRepository;
import hello.login.domain.service.OphthalmologyService;
import hello.login.domain.service.ReservationService;
import hello.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final OphthalmologyService ophthalmologyService;
    private final UserService userService;
    private final OphthalmologyRepository ophthalmologyRepository;

    // 예약 생성 폼 표시
    @GetMapping("/create")
    public String createReservationForm(@RequestParam("ophthalmologyId") Long ophthalmologyId, Model model) {
        PrincipalDetail principal = (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        Ophthalmology selectedOphthalmology = ophthalmologyService.getOphthalmologyById(ophthalmologyId);

        model.addAttribute("reservationForm", new ReservationForm());
        model.addAttribute("selectedOphthalmology", selectedOphthalmology);
        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
        model.addAttribute("patientName", user.getName());
        model.addAttribute("contactNumber", user.getNumber());
        model.addAttribute("ophthalmologyId", ophthalmologyId);
        model.addAttribute("ophthalmologyName", selectedOphthalmology.getName());

        return "reservation/reservationForm";
    }

    @PostMapping("/create")
    public String createReservation(
        @RequestParam Long ophthalmologyId,
        @ModelAttribute ReservationForm reservationForm,
        Authentication authentication,
        RedirectAttributes redirectAttributes) { // RedirectAttributes 추가
    User user = userService.findByUsername(authentication.getName());

    // 예약 정보에 Ophthalmology ID 설정
    reservationForm.setOphthalmologyId(ophthalmologyId);

    // 예약 생성
    Reservation createdReservation = reservationService.save(reservationForm, user);

    // 성공 메시지 추가
    redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 완료되었습니다.");

    // 예약 성공 후 리다이렉트
    return "redirect:/user/reservations"; // 예약 성공 페이지로 리다이렉트
    }

    // 사용자 예약 페이지
    @GetMapping
    public String reservationPage(Principal principal, Model model) {
        User user = ((PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        List<Reservation> reservations = reservationService.getReservationsByUser(user.getId());
        model.addAttribute("reservations", reservations);

        return "reservation/userReservations";
    }

    // 예약 수정 폼 표시
    @GetMapping("/user/reservations/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);

        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setId(reservation.getId());
        reservationForm.setReservationDate(reservation.getReservationDate());
        reservationForm.setReservationTime(reservation.getReservationTime());
        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());

        List<Ophthalmology> ophthalmologies = ophthalmologyService.getAllOphthalmologies();
        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("ophthalmologies", ophthalmologies);
        model.addAttribute("ophthalmologyName", reservation.getOphthalmology().getName());

        return "reservation/editReservationForm";
    }

    // 예약 수정 처리
    @PostMapping("/user/reservations/edit/{id}")
    public String editReservation(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm) {
        User user = ((PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        reservationService.updateReservation(id, reservationForm, user);
        return "redirect:/user/reservations";
    }

    @PostMapping("/user/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetail principal) {
        User user = principal.getUser(); // 현재 로그인한 사용자 정보 가져오기
        reservationService.deleteReservation(id, user);
        return "redirect:/user/reservations"; // 삭제 후 리다이렉트
    }
    // 예약 가능한 시간 가져오기
    @GetMapping("/available-times")
    public ResponseEntity<List<String>> getAvailableTimes(@RequestParam Long ophthalmologyId, @RequestParam String date) {
        LocalDate reservationDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        List<LocalTime> availableTimes = reservationService.getAvailableTimes(ophthalmologyId, reservationDate);

        List<String> availableTimeStrings = availableTimes.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());

        return ResponseEntity.ok(availableTimeStrings);
    }
}

