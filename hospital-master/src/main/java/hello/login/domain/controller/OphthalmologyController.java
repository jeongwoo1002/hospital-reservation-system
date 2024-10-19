package hello.login.domain.controller;

import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Ophthalmology;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.OphthalmologyRepository;
import hello.login.domain.service.OphthalmologyService;
import hello.login.domain.service.ReservationService;
import hello.login.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ophthalmology")
@RequiredArgsConstructor
public class OphthalmologyController {

    private final OphthalmologyRepository ophthalmologyRepository;
    private final OphthalmologyService ophthalmologyService;
    private final ReservationService reservationService;
    private final UserService userService;


    @GetMapping("/list")
    public String getOphthalmologyList(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
        int pageSize = 10; // 한 페이지에 보여줄 항목 수
        Page<Ophthalmology> ophthalmologyPage;
        if (keyword != null && !keyword.isEmpty()) {
            ophthalmologyPage = ophthalmologyService.searchByAddress(keyword, PageRequest.of(page, pageSize));
        } else {
            ophthalmologyPage = ophthalmologyRepository.findAll(PageRequest.of(page, pageSize));
        }
        model.addAttribute("ophthalmologyPage", ophthalmologyPage);
        model.addAttribute("page", ophthalmologyPage);
        model.addAttribute("keyword", keyword);
        return "ophthalmology/ophthalmologyList";
    }

//    @GetMapping("/detail/{id}")
//    public String getOphthalmologyDetail(@PathVariable Long id, Model model) {
//        Ophthalmology ophthalmology = ophthalmologyService.getOphthalmologyById(id);
//        model.addAttribute("ophthalmology", ophthalmology);
//        return "ophthalmology/ophthalmologyDetail";
//    }

    @GetMapping("/detail/{id}")
    public String getOphthalmologyDetail(@PathVariable Long id, Model model) {
        Ophthalmology ophthalmology = ophthalmologyService.getOphthalmologyById(id);
        model.addAttribute("ophthalmology", ophthalmology);

        // 주소를 KakaoMap에서 사용할 수 있도록 추가
        model.addAttribute("hospitalAddress", ophthalmology.getAddress());

        return "ophthalmology/ophthalmologyDetail";
    }

    @GetMapping("/reservation/new")
    public String createReservationForm(@RequestParam Long ophthalmologyId, Model model) {
        Ophthalmology ophthalmology = ophthalmologyService.getOphthalmologyById(ophthalmologyId);
        Reservation reservation = new Reservation();
        reservation.setOphthalmology(ophthalmology);
        model.addAttribute("reservation", reservation);
        return "reservation/reservationForm"; // reservation/reservationForm.html 경로가 맞는지 확인
    }



    @PostMapping("/reservation/new")
    public String createReservation(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ReservationForm reservationForm) {
        User user = userService.findByUsername(userDetails.getUsername());
        reservationService.saveReservation(reservationForm, user);
        return "redirect:/user/reservations";
    }

    @GetMapping("/reservation/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setId(reservation.getId());
        reservationForm.setReservationDate(reservation.getReservationDate());
        reservationForm.setReservationTime(reservation.getReservationTime());
        reservationForm.setOphthalmologyId(reservation.getOphthalmology().getId());
        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("ophthalmologies", ophthalmologyService.getAllOphthalmologies());
        return "reservation/editReservationForm";
    }

    @PostMapping("/reservation/edit")
    public String editReservation(@ModelAttribute ReservationForm reservationForm, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        reservationService.updateReservation(reservationForm.getId(), reservationForm, user);
        return "redirect:/user/reservations";
    }

    @PostMapping("/reservation/delete/{id}")
    public String deleteReservation(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()); // 사용자 정보 가져오기
        reservationService.deleteReservation(id, user); // 사용자 정보를 함께 전달
        return "redirect:/user/reservations";
    }


    @GetMapping("/user/reservations")
    public String getUserReservations(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Reservation> reservations = reservationService.getReservationsByUser(user.getId());

        // 로그 추가

        model.addAttribute("reservations", reservations);
        return "reservation/userReservations"; // userReservations.html로 이동
    }


//
//    @GetMapping("/nearby")
//    public List<Ophthalmology> getNearbyOphthalmology(@RequestParam double lat, @RequestParam double lng) {
//        return ophthalmologyService.findNearbyOphthalmologies(lat, lng);
//    }
}
