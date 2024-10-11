package hello.login.domain.controller;

import hello.login.domain.config.auth.PrincipalDetail;
import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Reservation;
import hello.login.domain.service.OphthalmologyService;
import hello.login.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final OphthalmologyService ophthalmologyService;

    @PostMapping("/reservation")
    public String createReservation(@ModelAttribute ReservationForm reservationForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Invalid date or times format");
            return "reservation/reservationForm";
        }
        return "redirect:/reservationSuccess";
    }
    /**
     * 예약 생성 폼
     */
    @GetMapping("/reservation/create")
    public String createForm(@RequestParam(required = false) Long ophthalmologyId, Model model) {
        logger.info("cresateForm called with ophthalmologyId: {}", ophthalmologyId);
        ReservationForm reservationForm = new ReservationForm();
        if (ophthalmologyId != null) {
            reservationForm.setOphthalmologyId(ophthalmologyId);
        }
        logger.info("ReservationForm initialized: {}", reservationForm);
        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
        return "reservation/reservationForm";
    }

//    @GetMapping("/create")
//    public String createForm(Model model) {
//        model.addAttribute("reservationForm", new ReservationForm());
//        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
//        return "reservation/reservationForm";
//    }

    /**
     * 예약 생성
     */
    @PostMapping("/reservation/create")
    public String create(@ModelAttribute ReservationForm reservationForm,
                         @AuthenticationPrincipal PrincipalDetail principal) {
        logger.info("Creating reservation: {}", reservationForm);
        reservationForm.setPatientName(principal.getUser().getUsername());
        reservationForm.setContactNumber(principal.getUser().getNumber());
        reservationService.saveReservation(reservationForm, principal.getUser());
        return "redirect:/reservation/list";
    }

//    @PostMapping("/create")
//    public String create(@ModelAttribute ReservationForm reservationForm,
//                         @AuthenticationPrincipal PrincipalDetail principal) {
//        reservationService.saveReservation(reservationForm, principal.getUser());
//        return "redirect:/reservation/list";
//    }

    /**
     * 예약 목록
     */
    @GetMapping("/reservation/list")
    public String list(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
        model.addAttribute("reservations", reservationService.getReservationsByUser(principal.getUser().getId()));
        return "reservation/reservationList";
    }

    /**
     * 예약 상세 보기
     */
    @GetMapping("/reservation/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("reservation", reservationService.getReservationById(id));
        return "reservation/reservationDetail";
    }

    @GetMapping("/user/reservations")
    public String getUserReservations(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
        List<Reservation> reservations = reservationService.getReservationsByUser(principal.getUser().getId());
        model.addAttribute("reservations", reservations);
        return "user/userReservations";
    }

    /**
     * 예약 수정 폼
     */
    @GetMapping("/reservation/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        logger.info("editForm called with id: {}", id);
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            logger.error("Reservation not found for id: {}", id);
            return "error/404";
        }
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setPatientName(reservation.getPatientName());
        reservationForm.setContactNumber(reservation.getContactNumber());
        reservationForm.setReservationDate(reservation.getReservationDate());
        reservationForm.setReservationTime(reservation.getReservationTime());
        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());

        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
        return "reservation/editReservationForm";
    }

    /**
     * 예약 수정
     */
    @PostMapping("/reservation/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm,
                       @AuthenticationPrincipal PrincipalDetail principal) {
        logger.info("edit called with id: {}, reservationForm: {}", id, reservationForm);
        reservationService.updateReservation(id, reservationForm, principal.getUser());
        return "redirect:/reservation/list";
    }

    /**
     * 예약 삭제
     */
    @PostMapping("/reservation/delete/{id}")
    public String delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservation/list";
    }

    @GetMapping("/reservation/available-times")
    public List<String> getAvailableTimes(@RequestParam Long ophthalmologyId, @RequestParam LocalDate date) {
        List<LocalTime> availableTimes = reservationService.getBookedTimes(ophthalmologyId, date);
        return availableTimes.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());
    }
}


//
//package hello.login.domain.controller;
//
//import hello.login.domain.config.auth.PrincipalDetail;
//import hello.login.domain.dto.ReservationForm;
//import hello.login.domain.model.Reservation;
//import hello.login.domain.service.OphthalmologyService;
//import hello.login.domain.service.ReservationService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@RequiredArgsConstructor
//public class ReservationController {
//
//    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
//
//    private final ReservationService reservationService;
//    private final OphthalmologyService ophthalmologyService;
//
//    @GetMapping("/reservation/create")
//    public String createForm(@RequestParam(required = false) Long ophthalmologyId, Model model) {
//        logger.info("createForm called with ophthalmologyId: {}", ophthalmologyId);
//        ReservationForm reservationForm = new ReservationForm();
//        if (ophthalmologyId != null) {
//            reservationForm.setOphthalmologyId(ophthalmologyId);
//        }
//        model.addAttribute("reservationForm", reservationForm);
//        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
//        return "reservation/reservationForm";
//    }
//
//    @PostMapping("/reservation/create")
//    public String create(@ModelAttribute ReservationForm reservationForm,
//                         @AuthenticationPrincipal PrincipalDetail principal) {
//        logger.info("Creating reservation: {}", reservationForm);
//        try {
//            reservationForm.setPatientName(principal.getUser().getUsername());
//            reservationForm.setContactNumber(principal.getUser().getNumber());
//            reservationService.saveReservation(reservationForm, principal.getUser());
//        } catch (IllegalArgumentException e) {
//            logger.error("Error creating reservation: {}", e.getMessage());
//            return "error/errorPage"; // 사용자에게 오류를 알리는 페이지로 리디렉션
//        }
//        return "redirect:/reservation/list";
//    }
//
//    @GetMapping("/reservation/list")
//    public String list(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
//        model.addAttribute("reservations", reservationService.getReservationsByUser(principal.getUser().getId()));
//        return "user/userReservations";
//    }
//
//    @GetMapping("/reservation/detail/{id}")
//    public String detail(@PathVariable Long id, Model model) {
//        model.addAttribute("reservation", reservationService.getReservationById(id));
//        return "reservation/reservationDetail";
//    }
//
//    @GetMapping("/reservation/edit/{id}")
//    public String editForm(@PathVariable Long id, Model model) {
//        logger.info("editForm called with id: {}", id);
//        Reservation reservation = reservationService.getReservationById(id);
//        if (reservation == null) {
//            logger.error("Reservation not found for id: {}", id);
//            return "error/404";
//        }
//        ReservationForm reservationForm = new ReservationForm();
//        reservationForm.setPatientName(reservation.getPatientName());
//        reservationForm.setContactNumber(reservation.getContactNumber());
//        reservationForm.setReservationDate(reservation.getReservationDate());
//        reservationForm.setReservationTime(reservation.getReservationTime());
//        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
//
//        model.addAttribute("reservationForm", reservationForm);
//        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
//        return "reservation/editReservationForm";
//    }
//
//    @PostMapping("/reservation/edit/{id}")
//    public String edit(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm,
//                       @AuthenticationPrincipal PrincipalDetail principal) {
//        logger.info("edit called with id: {}, reservationForm: {}", id, reservationForm);
//        try {
//            reservationService.updateReservation(id, reservationForm, principal.getUser());
//        } catch (IllegalArgumentException e) {
//            logger.error("Error updating reservation: {}", e.getMessage());
//            return "error/errorPage";
//        }
//        return "redirect:/reservation/list";
//    }
//
//    @PostMapping("/reservation/delete/{id}")
//    public String delete(@PathVariable Long id) {
//        reservationService.deleteReservation(id);
//        return "redirect:/reservation/list";
//    }
//
//    @GetMapping("/reservation/available-times")
//    @ResponseBody
//    public List<String> getAvailableTimes(@RequestParam Long ophthalmologyId, @RequestParam LocalDate date) {
//        return reservationService.getAvailableTimes(ophthalmologyId, date);
//    }
//
//    @GetMapping("/hospitalUser/reservations")
//    public String getHospitalUserReservations(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
//        Long hospitalUserId = principal.getUser().getId();
//        List<Reservation> reservations = reservationService.getReservationsByHospitalUser(hospitalUserId);
//        model.addAttribute("reservations", reservations);
//        return "hospitalUser/hospitalUserReservations";
//    }
//
//    @GetMapping("/hospitalUser/reservation/edit/{id}")
//    public String hospitalUserEditForm(@PathVariable Long id, Model model) {
//        logger.info("hospitalUserEditForm called with id: {}", id);
//        Reservation reservation = reservationService.getReservationById(id);
//        if (reservation == null) {
//            logger.error("Reservation not found for id: {}", id);
//            return "error/404";
//        }
//        ReservationForm reservationForm = new ReservationForm();
//        reservationForm.setPatientName(reservation.getPatientName());
//        reservationForm.setContactNumber(reservation.getContactNumber());
//        reservationForm.setReservationDate(reservation.getReservationDate());
//        reservationForm.setReservationTime(reservation.getReservationTime());
//        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
//
//        model.addAttribute("reservationForm", reservationForm);
//        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
//        return "hospitalUser/editHospitalUserReservationForm";
//    }
//
//    @PostMapping("/hospitalUser/reservation/edit/{id}")
//    public String hospitalUserEdit(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm,
//                                   @AuthenticationPrincipal PrincipalDetail principal) {
//        logger.info("hospitalUserEdit called with id: {}, reservationForm: {}", id, reservationForm);
//        try {
//            reservationService.updateReservation(id, reservationForm, principal.getUser());
//        } catch (IllegalArgumentException e) {
//            logger.error("Error updating reservation: {}", e.getMessage());
//            return "error/errorPage";
//        }
//        return "redirect:/hospitalUser/reservations";
//    }
//
//    @PostMapping("/hospitalUser/reservation/delete/{id}")
//    public String hospitalUserDelete(@PathVariable Long id) {
//        reservationService.deleteReservation(id);
//        return "redirect:/hospitalUser/reservations";
//    }
//}
//
//
////package hello.login.domain.controller;
////
////import hello.login.domain.config.auth.PrincipalDetail;
////import hello.login.domain.dto.ReservationForm;
////import hello.login.domain.model.Reservation;
////import hello.login.domain.service.OphthalmologyService;
////import hello.login.domain.service.ReservationService;
////import lombok.RequiredArgsConstructor;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.security.core.annotation.AuthenticationPrincipal;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.validation.BindingResult;
////import org.springframework.web.bind.annotation.*;
////
////import javax.validation.Valid;
////import java.time.LocalDate;
////import java.time.LocalTime;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Controller
////@RequiredArgsConstructor
////public class ReservationController {
////
////    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
////
////    private final ReservationService reservationService;
////    private final OphthalmologyService ophthalmologyService;
////
////    /**
////     * 예약 생성 폼 (사용자 및 병원 관리자)
////     */
////    @GetMapping("/reservation/create")
////    public String createForm(@RequestParam(required = false) Long ophthalmologyId, Model model) {
////        logger.info("createForm called with ophthalmologyId: {}", ophthalmologyId);
////        ReservationForm reservationForm = new ReservationForm();
////        if (ophthalmologyId != null) {
////            reservationForm.setOphthalmologyId(ophthalmologyId);
////        }
////        logger.info("ReservationForm initialized: {}", reservationForm);
////        model.addAttribute("reservationForm", reservationForm);
////        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
////        return "reservation/reservationForm";
////    }
////
////    /**
////     * 예약 생성 (사용자)
////     */
////    @PostMapping("/reservation/create")
////    public String create(@ModelAttribute ReservationForm reservationForm,
////                         @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("Creating reservation: {}", reservationForm);
////        reservationForm.setPatientName(principal.getUser().getUsername());
////        reservationForm.setContactNumber(principal.getUser().getNumber());
////        reservationService.saveReservation(reservationForm, principal.getUser());
////        return "redirect:/reservation/list";
////    }
////
////    /**
////     * 사용자 예약 목록
////     */
////    @GetMapping("/reservation/list")
////    public String list(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
////        model.addAttribute("reservations", reservationService.getReservationsByUser(principal.getUser().getId()));
////        return "user/userReservations";
////    }
////
////    /**
////     * 사용자 예약 상세 보기
////     */
////    @GetMapping("/reservation/detail/{id}")
////    public String detail(@PathVariable Long id, Model model) {
////        model.addAttribute("reservation", reservationService.getReservationById(id));
////        return "reservation/reservationDetail";
////    }
////
////    /**
////     * 예약 수정 폼 (사용자 및 병원 관리자)
////     */
////    @GetMapping("/reservation/edit/{id}")
////    public String editForm(@PathVariable Long id, Model model) {
////        logger.info("editForm called with id: {}", id);
////        Reservation reservation = reservationService.getReservationById(id);
////        if (reservation == null) {
////            logger.error("Reservation not found for id: {}", id);
////            return "error/404";
////        }
////        ReservationForm reservationForm = new ReservationForm();
////        reservationForm.setPatientName(reservation.getPatientName());
////        reservationForm.setContactNumber(reservation.getContactNumber());
////        reservationForm.setReservationDate(reservation.getReservationDate());
////        reservationForm.setReservationTime(reservation.getReservationTime());
////        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
////
////        model.addAttribute("reservationForm", reservationForm);
////        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
////        return "reservation/editReservationForm";
////    }
////
////    /**
////     * 예약 수정 (사용자 및 병원 관리자)
////     */
////    @PostMapping("/reservation/edit/{id}")
////    public String edit(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm,
////                       @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("edit called with id: {}, reservationForm: {}", id, reservationForm);
////        reservationService.updateReservation(id, reservationForm, principal.getUser());
////        return "redirect:/reservation/list";
////    }
////
////    /**
////     * 예약 삭제 (사용자 및 병원 관리자)
////     */
////    @PostMapping("/reservation/delete/{id}")
////    public String delete(@PathVariable Long id) {
////        reservationService.deleteReservation(id);
////        return "redirect:/reservation/list";
////    }
////
////    /**
////     * 예약 가능 시간 조회
////     */
////    @GetMapping("/reservation/available-times")
////    @ResponseBody
////    public List<String> getAvailableTimes(@RequestParam Long ophthalmologyId, @RequestParam LocalDate date) {
////        List<LocalTime> availableTimes = reservationService.getBookedTimes(ophthalmologyId, date);
////        return availableTimes.stream()
////                .map(LocalTime::toString)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * 병원 관리자의 예약 목록
////     */
////    @GetMapping("/hospitalUser/reservations")
////    public String getHospitalUserReservations(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
////        Long hospitalUserId = principal.getUser().getId(); // Assuming `hospitalUserId` can be obtained from `User`
////        List<Reservation> reservations = reservationService.getReservationsByHospitalUser(hospitalUserId);
////        model.addAttribute("reservations", reservations);
////        return "hospitalUser/hospitalUserReservations"; // 병원 관리자 예약 페이지
////    }
////
////    /**
////     * 병원 관리자의 예약 수정 폼
////     */
////    @GetMapping("/hospitalUser/reservation/edit/{id}")
////    public String hospitalUserEditForm(@PathVariable Long id, Model model) {
////        logger.info("hospitalUserEditForm called with id: {}", id);
////        Reservation reservation = reservationService.getReservationById(id);
////        if (reservation == null) {
////            logger.error("Reservation not found for id: {}", id);
////            return "error/404";
////        }
////        ReservationForm reservationForm = new ReservationForm();
////        reservationForm.setPatientName(reservation.getPatientName());
////        reservationForm.setContactNumber(reservation.getContactNumber());
////        reservationForm.setReservationDate(reservation.getReservationDate());
////        reservationForm.setReservationTime(reservation.getReservationTime());
////        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
////
////        model.addAttribute("reservationForm", reservationForm);
////        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
////        return "hospitalUser/editHospitalUserReservationForm"; // 병원 관리자 예약 수정 폼
////    }
////
////    /**
////     * 병원 관리자의 예약 수정
////     */
////    @PostMapping("/hospitalUser/reservation/edit/{id}")
////    public String hospitalUserEdit(@PathVariable Long id, @ModelAttribute ReservationForm reservationForm,
////                                   @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("hospitalUserEdit called with id: {}, reservationForm: {}", id, reservationForm);
////        reservationService.updateReservation(id, reservationForm, principal.getUser());
////        return "redirect:/hospitalUser/reservations";
////    }
////
////    /**
////     * 병원 관리자의 예약 삭제
////     */
////    @PostMapping("/hospitalUser/reservation/delete/{id}")
////    public String hospitalUserDelete(@PathVariable Long id) {
////        reservationService.deleteReservation(id);
////        return "redirect:/hospitalUser/reservations";
////    }
////}
////@Controller
////@RequiredArgsConstructor
////public class ReservationController {
////
////    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
////
////    private final ReservationService reservationService;
////    private final OphthalmologyService ophthalmologyService;
////
////    // 예약 생성 폼
////    @GetMapping("/reservation/create")
////    public String createForm(@RequestParam(required = false) Long ophthalmologyId, Model model) {
////        logger.info("createForm called with ophthalmologyId: {}", ophthalmologyId);
////
////        ReservationForm reservationForm = new ReservationForm();
////        if (ophthalmologyId != null) {
////            reservationForm.setOphthalmologyId(ophthalmologyId);
////        }
////
////        model.addAttribute("reservationForm", reservationForm);
////        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
////        return "reservation/reservationForm";
////    }
////
////    // 예약 생성
////    @PostMapping("/reservation/create")
////    public String create(@ModelAttribute @Valid ReservationForm reservationForm, BindingResult result,
////                         @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("Creating reservation: {}", reservationForm);
////
////        // 유효성 검증 실패 시 다시 폼으로 이동
////        if (result.hasErrors()) {
////            logger.error("Form validation errors: {}", result.getAllErrors());
////            return "reservation/reservationForm";
////        }
////
////        try {
////            // 사용자 정보 설정
////            reservationForm.setPatientName(principal.getUser().getUsername());
////            reservationForm.setContactNumber(principal.getUser().getNumber());
////
////            // 예약 저장 로직 호출
////            reservationService.saveReservation(reservationForm, principal.getUser());
////        } catch (IllegalArgumentException e) {
////            logger.error("Error creating reservation: {}", e.getMessage());
////            return "error/errorPage";  // 오류 페이지로 이동
////        }
////
////        return "redirect:/reservation/list";  // 성공 시 예약 목록 페이지로 리디렉션
////    }
////
////    // 예약 목록 조회
////    @GetMapping("/reservation/list")
////    public String list(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
////        model.addAttribute("reservations", reservationService.getReservationsByUser(principal.getUser().getId()));
////        return "user/userReservations";
////    }
////
////    // 예약 상세 조회
////    @GetMapping("/reservation/detail/{id}")
////    public String detail(@PathVariable Long id, Model model) {
////        model.addAttribute("reservation", reservationService.getReservationById(id));
////        return "reservation/reservationDetail";
////    }
////
////    // 예약 수정 폼
////    @GetMapping("/reservation/edit/{id}")
////    public String editForm(@PathVariable Long id, Model model) {
////        logger.info("editForm called with id: {}", id);
////
////        Reservation reservation = reservationService.getReservationById(id);
////        if (reservation == null) {
////            logger.error("Reservation not found for id: {}", id);
////            return "error/404";  // 예약이 없는 경우 404 페이지로 이동
////        }
////
////        ReservationForm reservationForm = new ReservationForm();
////        reservationForm.setPatientName(reservation.getPatientName());
////        reservationForm.setContactNumber(reservation.getContactNumber());
////        reservationForm.setReservationDate(reservation.getReservationDate());
////        reservationForm.setReservationTime(reservation.getReservationTime());
////        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
////
////        model.addAttribute("reservationForm", reservationForm);
////        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
////        return "reservation/editReservationForm";
////    }
////
////    // 예약 수정
////    @PostMapping("/reservation/edit/{id}")
////    public String edit(@PathVariable Long id, @ModelAttribute @Valid ReservationForm reservationForm, BindingResult result,
////                       @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("edit called with id: {}, reservationForm: {}", id, reservationForm);
////
////        if (result.hasErrors()) {
////            logger.error("Form validation errors: {}", result.getAllErrors());
////            return "reservation/editReservationForm";
////        }
////
////        try {
////            reservationService.updateReservation(id, reservationForm, principal.getUser());
////        } catch (IllegalArgumentException e) {
////            logger.error("Error updating reservation: {}", e.getMessage());
////            return "error/errorPage";
////        }
////
////        return "redirect:/reservation/list";
////    }
////
////    // 예약 삭제
////    @PostMapping("/reservation/delete/{id}")
////    public String delete(@PathVariable Long id) {
////        reservationService.deleteReservation(id);
////        return "redirect:/reservation/list";
////    }
////
////    // 예약 가능한 시간 조회
////    @GetMapping("/reservation/available-times")
////    @ResponseBody
////    public List<String> getAvailableTimes(@RequestParam Long ophthalmologyId, @RequestParam LocalDate date) {
////        return reservationService.getAvailableTimes(ophthalmologyId, date);
////    }
////
////    // 병원 사용자 예약 목록 조회
////    @GetMapping("/hospitalUser/reservations")
////    public String getHospitalUserReservations(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
////        Long hospitalUserId = principal.getUser().getId();
////        List<Reservation> reservations = reservationService.getReservationsByHospitalUser(hospitalUserId);
////        model.addAttribute("reservations", reservations);
////        return "hospitalUser/hospitalUserReservations";
////    }
////
////    // 병원 사용자 예약 수정 폼
////    @GetMapping("/hospitalUser/reservation/edit/{id}")
////    public String hospitalUserEditForm(@PathVariable Long id, Model model) {
////        logger.info("hospitalUserEditForm called with id: {}", id);
////
////        Reservation reservation = reservationService.getReservationById(id);
////        if (reservation == null) {
////            logger.error("Reservation not found for id: {}", id);
////            return "error/404";
////        }
////
////        ReservationForm reservationForm = new ReservationForm();
////        reservationForm.setPatientName(reservation.getPatientName());
////        reservationForm.setContactNumber(reservation.getContactNumber());
////        reservationForm.setReservationDate(reservation.getReservationDate());
////        reservationForm.setReservationTime(reservation.getReservationTime());
////        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
////
////        model.addAttribute("reservationForm", reservationForm);
////        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
////        return "hospitalUser/editHospitalUserReservationForm";
////    }
////
////    // 병원 사용자 예약 수정
////    @PostMapping("/hospitalUser/reservation/edit/{id}")
////    public String hospitalUserEdit(@PathVariable Long id, @ModelAttribute @Valid ReservationForm reservationForm,
////                                   BindingResult result, @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("hospitalUserEdit called with id: {}, reservationForm: {}", id, reservationForm);
////
////        if (result.hasErrors()) {
////            logger.error("Form validation errors: {}", result.getAllErrors());
////            return "hospitalUser/editHospitalUserReservationForm";
////        }
////
////        try {
////            reservationService.updateReservation(id, reservationForm, principal.getUser());
////        } catch (IllegalArgumentException e) {
////            logger.error("Error updating reservation: {}", e.getMessage());
////            return "error/errorPage";
////        }
////
////        return "redirect:/hospitalUser/reservations";
////    }
////
////    // 병원 사용자 예약 삭제
////    @PostMapping("/hospitalUser/reservation/delete/{id}")
////    public String hospitalUserDelete(@PathVariable Long id) {
////        reservationService.deleteReservation(id);
////        return "redirect:/hospitalUser/reservations";
////    }
////    @PostMapping("/reservation/save")
////    public String saveReservation(@ModelAttribute ReservationForm reservationForm,
////                                  @AuthenticationPrincipal PrincipalDetail principal) {
////        logger.info("Saving reservation: {}", reservationForm);
////        try {
////            reservationForm.setPatientName(principal.getUser().getUsername());
////            reservationForm.setContactNumber(principal.getUser().getNumber());
////            reservationService.saveReservation(reservationForm, principal.getUser());
////        } catch (IllegalArgumentException e) {
////            logger.error("Error saving reservation: {}", e.getMessage());
////            return "error/errorPage";
////        }
////        return "redirect:/reservation/list";
////    }
////
////}
