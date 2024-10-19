package hello.login.domain.controller;

import hello.login.domain.config.auth.PrincipalDetail;
import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Ophthalmology;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.ReservationRepository;
import hello.login.domain.service.OphthalmologyService;
import hello.login.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final OphthalmologyService ophthalmologyService;
    private final ReservationRepository reservationRepository;

    @GetMapping("/create")
    public String createReservationForm(@RequestParam("ophthalmologyId")Long ophthalmologyId, Model model) {
        PrincipalDetail principal = (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser(); // PrincipalDetail에서 User 정보 가져오기

        Ophthalmology selectedOphthalmology = ophthalmologyService.getOphthalmologyById(ophthalmologyId);

        model.addAttribute("reservationForm", new ReservationForm());
        model.addAttribute("selectedOphthalmology", ophthalmologyService.getOphthalmologyById(ophthalmologyId)); // 선택된 병원 정보
        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
        model.addAttribute("patientName", user.getName()); // 사용자 이름 추가
        model.addAttribute("contactNumber", user.getNumber()); // 사용자 연락처 추가
        model.addAttribute("ophthalmologyName", selectedOphthalmology.getName()); // 안과병원 이름 추가
        return "reservation/reservationForm"; // 생성 폼 뷰
    }

    // 예약 생성 처리
    @PostMapping("/create")
    public String createReservation(@ModelAttribute ReservationForm reservationForm) {
        PrincipalDetail principal = (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser(); // 사용자 정보 가져오기

        // 예약 정보 저장
        reservationService.saveReservation(reservationForm, user);

        return "redirect:/user/reservations"; // 예약 목록 페이지로 리다이렉트
    }
//    // 예약 생성 처리
//    @PostMapping
//    public String createReservation(@ModelAttribute ReservationForm reservationForm, HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        reservationService.saveReservation(reservationForm, user);
//        return "redirect:/reservation"; // 예약 목록 페이지로 리다이렉트
//    }

    // 예약 목록 페이지
    @GetMapping
    public String listUserReservations(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Reservation> reservations = reservationService.getReservationsByUser(user.getId());
        model.addAttribute("reservations", reservations);
        return "reservation/userReservations"; // 예약 목록 뷰
    }

    // 예약 수정 페이지
//    @GetMapping("/user/reservations/edit/{id}")
//    public String editReservationForm(@PathVariable Long id, Model model, HttpSession session) {
//        Reservation reservation = reservationService.getReservationById(id);
//        model.addAttribute("reservationForm", reservation);
//        return "reservation/editReservationForm"; // 수정 폼 템플릿 경로
//    }

    @GetMapping("/user/reservations/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model, HttpSession session) {
        Reservation reservation = reservationService.getReservationById(id);
        ReservationForm reservationForm = new ReservationForm();

        // 기존 예약 정보를 DTO에 설정
        reservationForm.setId(reservation.getId());
        reservationForm.setReservationDate(reservation.getReservationDate());
        reservationForm.setReservationTime(reservation.getReservationTime());
        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId()); // 안과 ID 설정

        // 안과 목록을 모델에 추가
        List<Ophthalmology> ophthalmologies = ophthalmologyService.getAllOphthalmologies();

        model.addAttribute("reservationForm", reservationForm); // 수정할 예약 데이터 전달
        model.addAttribute("ophthalmologies", ophthalmologies); // 안과 목록 전달
        model.addAttribute("ophthalmologyName", reservation.getOphthalmology().getName()); // 안과병원 이름 추가

        return "reservation/editReservationForm"; // 수정 폼 뷰
    }

    // 예약 수정 처리
//    @PostMapping("/user/reservations/edit/{id}")
//    public String editReservation(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm, HttpSession session) {
//        User user = (User) session.getAttribute("user");
//
//        // reservationForm에 ID를 설정합니다.
//        reservationForm.setId(id);
//
//        // 예약 정보를 업데이트합니다.
//        reservationService.updateReservation(id, reservationForm, user);
//
//        // 예약 목록 페이지로 리다이렉트합니다.
//        return "redirect:/user/reservations";
//    }
    @PostMapping("/user/reservations/edit/{id}")
    public String editReservation(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm, HttpSession session) {
        User user = (User) session.getAttribute("user");
        reservationService.updateReservation(id, reservationForm, user);
        return "redirect:/user/reservations"; // 예약 목록 페이지로 리다이렉트
    }

    // 예약 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        reservationService.deleteReservation(id, user);
        return "redirect:/reservation"; // 예약 목록 페이지로 리다이렉트
    }

    // 예약 가능한 시간 가져오기
    @GetMapping("/available-times")
    public List<LocalTime> getAvailableTimes(@RequestParam Long ophthalmologyId, @RequestParam String date) {
        LocalDate reservationDate = LocalDate.parse(date);
        List<LocalTime> availableTimes = new ArrayList<>();

        // 예약 가능한 시간 생성 (09:00부터 18:00까지 15분 단위)
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(15)) {
            // 점심시간 제외
            if (Reservation.isTimeSlotAvailable(time, reservationDate)) {
                availableTimes.add(time);
            }
        }

        // 이미 예약된 시간은 제외
        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, reservationDate);
        for (Reservation reservation : reservations) {
            availableTimes.remove(reservation.getReservationTime());
        }

        return availableTimes;
    }
}
