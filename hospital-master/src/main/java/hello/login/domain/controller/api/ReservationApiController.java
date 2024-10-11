////package hello.login.domain.controller.api;
////
////import hello.login.domain.config.auth.PrincipalDetail;
////import hello.login.domain.dto.ReservationForm;
////import hello.login.domain.dto.ResponseDto;
////import hello.login.domain.service.ReservationService;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.HttpStatus;
////import org.springframework.security.core.annotation.AuthenticationPrincipal;
////import org.springframework.web.bind.annotation.*;
////
////@RestController
////@RequiredArgsConstructor
////@RequestMapping("/api/reservation")
////public class ReservationApiController {
////
////    private final ReservationService reservationService;
////
////    @PostMapping
////    public ResponseDto<Integer> createReservation(@RequestBody ReservationForm reservationForm,
////                                                  @AuthenticationPrincipal PrincipalDetail principal) {
////        reservationService.saveReservation(reservationForm, principal.getUser());
////        return new ResponseDto<>(HttpStatus.OK.value(), 1);
////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseDto<Integer> deleteReservation(@PathVariable Long id) {
////        reservationService.deleteReservation(id);
////        return new ResponseDto<>(HttpStatus.OK.value(), 1);
////    }
////}
//package hello.login.domain.controller.api;
//
//import hello.login.domain.config.auth.PrincipalDetail;
//import hello.login.domain.dto.ReservationForm;
//import hello.login.domain.dto.ResponseDto;
//import hello.login.domain.service.ReservationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/reservation")
//public class ReservationApiController {
//
//    private final ReservationService reservationService;
//
//    // 예약 생성
//    @PostMapping
//    public ResponseDto<Integer> createReservation(@RequestBody ReservationForm reservationForm,
//                                                  @AuthenticationPrincipal PrincipalDetail principal) {
//        reservationService.saveReservation(reservationForm, principal.getUser());
//        return new ResponseDto<>(HttpStatus.OK.value(), 1);
//    }
//
//    // 예약 삭제
//    @DeleteMapping("/{id}")
//    public ResponseDto<Integer> deleteReservation(@PathVariable Long id) {
//        reservationService.deleteReservation(id);
//        return new ResponseDto<>(HttpStatus.OK.value(), 1);
//    }
//
//    @GetMapping("/available-dates")
//    public ResponseDto<List<LocalDate>> getAvailableDates(@RequestParam Long ophthalmologyId) {
//        List<LocalDate> availableDates = reservationService.getAvailableDates(ophthalmologyId);
//        return new ResponseDto<>(HttpStatus.OK.value(), availableDates);
//    }
//
//    // 병원과 날짜에 대한 예약 가능한 시간 목록 조회
//    @GetMapping("/available-times")
//    public ResponseDto<List<String>> getAvailableTimes(@RequestParam Long ophthalmologyId,
//                                                       @RequestParam LocalDate date) {
//        List<String> availableTimes = reservationService.getAvailableTimes(ophthalmologyId, date);
//        return new ResponseDto<>(HttpStatus.OK.value(), availableTimes);
//    }
//}
package hello.login.domain.controller.api;

import hello.login.domain.config.auth.PrincipalDetail;
import hello.login.domain.dto.ReservationForm;
import hello.login.domain.dto.ResponseDto;
import hello.login.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationApiController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseDto<Integer> createReservation(@RequestBody ReservationForm reservationForm,
                                                  @AuthenticationPrincipal PrincipalDetail principal) {
        reservationService.saveReservation(reservationForm, principal.getUser());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}