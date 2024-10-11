//package hello.login.domain.repository;
//
//import hello.login.domain.model.Reservation;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Repository
//public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//
//    List<Reservation> findByUserId(Long userId);
//
//    List<Reservation> findByOphthalmology_Id(Long ophthalmologyId);
//
//    List<Reservation> findByOphthalmologyIdAndReservationDate(Long ophthalmologyId, LocalDate reservationDate);
//
//    List<Reservation> findByOphthalmologyIdAndReservationDateAndReservationTimeBetween(
//            Long ophthalmologyId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime);
//
//    @Query("SELECT r FROM Reservation r WHERE r.ophthalmology.hospitalUser.id = :hospitalUserId")
//    List<Reservation> findReservationsByHospitalUserId(@Param("hospitalUserId") Long hospitalUserId);
//
//    // 병원 ID와 날짜에 대해 예약된 시간을 조회하는 메서드 추가
//    @Query("SELECT r.reservationTime FROM Reservation r WHERE r.ophthalmology.id = :ophthalmologyId AND r.reservationDate = :date")
//    List<LocalTime> findReservedTimesByOphthalmologyAndDate(@Param("ophthalmologyId") Long ophthalmologyId,
//                                                            @Param("date") LocalDate date);
//}
package hello.login.domain.repository;

import hello.login.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

}