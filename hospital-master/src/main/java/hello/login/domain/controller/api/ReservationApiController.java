package hello.login.domain.controller.api;

import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.UserRepository;
import hello.login.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;

    // 예약 생성
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationForm reservationForm, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Reservation reservation = reservationService.createReservation(reservationForm, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    // 예약 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReservation(@PathVariable Long id, @RequestBody ReservationForm reservationForm, HttpSession session) {
        User user = (User) session.getAttribute("user");
        reservationService.updateReservation(id, reservationForm, user);
        return ResponseEntity.ok("예약이 성공적으로 수정되었습니다.");
    }

    // 예약 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        reservationService.deleteReservation(id, user);
        return ResponseEntity.ok("예약이 성공적으로 삭제되었습니다.");
    }

    // 사용자 예약 목록 조회
    @GetMapping
    public ResponseEntity<List<Reservation>> getUserReservations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Reservation> reservations = reservationService.getReservationsByUser(user.getId());
        return ResponseEntity.ok(reservations);
    }
}
