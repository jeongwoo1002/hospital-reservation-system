package hello.login.domain.repository;

import hello.login.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByOphthalmologyId(Long ophthalmologyId);
    List<Reservation> findByOphthalmologyIdAndReservationDate(Long ophthalmologyId, LocalDate reservationDate);

    List<Reservation> findByOphthalmologyIdAndReservationDateAndReservationTime(Long ophthalmologyId, LocalDate reservationDate, LocalTime reservationTime);
    // 안과 ID와 예약 날짜를 기준으로 예약된 시간만 가져오는 메서드
    List<Reservation> findByReservationDateAndReservationTime(String reservationDate, String reservationTime);
}
