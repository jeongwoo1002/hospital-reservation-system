package hello.login.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;      // 예약자 이름
    private String contactNumber;    // 예약자 연락처

    private LocalDate reservationDate;   // 예약 날짜
    private LocalTime reservationTime;   // 예약 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 예약한 사용자 (User 엔티티와 연관)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ophthalmology_id", nullable = false)
    private Ophthalmology ophthalmology;  // 안과와의 관계 설정 (Ophthalmology 엔티티와 연관)

    /**
     * 예약 가능한 시간인지 확인하는 메서드
     * 점심 시간 (13:00~14:00), 평일(09:00~18:00) 외, 토요일(09:00~15:00) 외 시간은 예약 불가
     */
    public static boolean isTimeSlotAvailable(LocalTime time, LocalDate date) {
        boolean isLunchTime = time.isAfter(LocalTime.of(13, 0)) && time.isBefore(LocalTime.of(14, 0));
        boolean isSaturdayAfterHours = date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY && time.isAfter(LocalTime.of(15, 0));
        boolean isWeekdayOutsideHours = date.getDayOfWeek().getValue() < 6 && (time.isBefore(LocalTime.of(9, 0)) || time.isAfter(LocalTime.of(18, 0)));
        boolean isSunday = date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;

        return !(isLunchTime || isSaturdayAfterHours || isWeekdayOutsideHours || isSunday);
    }

    // 예약 생성 메서드
    public static Reservation createReservation(String patientName, String contactNumber, LocalDate reservationDate, LocalTime reservationTime, User user, Ophthalmology ophthalmology) {
        Reservation reservation = new Reservation();
        reservation.setPatientName(patientName);
        reservation.setContactNumber(contactNumber);
        reservation.setReservationDate(reservationDate);
        reservation.setReservationTime(reservationTime);
        reservation.setUser(user);
        reservation.setOphthalmology(ophthalmology);
        return reservation;
    }

    // 예약 수정 메서드
    public void updateReservation(LocalDate reservationDate, LocalTime reservationTime, Ophthalmology ophthalmology) {
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.ophthalmology = ophthalmology;
    }
}
