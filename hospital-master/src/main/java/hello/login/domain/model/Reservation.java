//package hello.login.domain.model;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Entity
//public class Reservation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String patientName;
//    private String contactNumber;
//    private LocalDate reservationDate;
//    private LocalTime reservationTime;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "ophthalmology_id")
//    private Ophthalmology ophthalmology;
//
//    // 병원 ID 추가
//    private Long hospitalId;
//
//    // 병원 운영 시간
//    private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
//    private static final LocalTime CLOSING_TIME = LocalTime.of(18, 0);
//
//    // 점심 시간
//    private static final LocalTime LUNCH_START = LocalTime.of(13, 0);
//    private static final LocalTime LUNCH_END = LocalTime.of(14, 0);
//
//    // 예약 시간 단위 (15분)
//    private static final int RESERVATION_INTERVAL_MINUTES = 15;
//
//    // 예약 가능 여부 확인
//    public boolean isReservable() {
//        // 과거 날짜 예약 불가
//        if (reservationDate.isBefore(LocalDate.now())) {
//            return false;
//        }
//
//        // 운영 시간 내에 있는지 확인
//        if (reservationTime.isBefore(OPENING_TIME) || reservationTime.plusMinutes(RESERVATION_INTERVAL_MINUTES).isAfter(CLOSING_TIME)) {
//            return false;
//        }
//
//        // 점심 시간 내 예약 불가
//        if (!reservationTime.isBefore(LUNCH_START) && reservationTime.isBefore(LUNCH_END)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    // 예약 시간이 중복되는지 확인
//    public boolean isTimeConflict(Reservation otherReservation) {
//        return this.reservationDate.equals(otherReservation.getReservationDate()) &&
//                !this.reservationTime.isBefore(otherReservation.getReservationTime()) &&
//                this.reservationTime.isBefore(otherReservation.getReservationTime().plusMinutes(RESERVATION_INTERVAL_MINUTES));
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getPatientName() {
//        return patientName;
//    }
//
//    public void setPatientName(String patientName) {
//        this.patientName = patientName;
//    }
//
//    public String getContactNumber() {
//        return contactNumber;
//    }
//
//    public void setContactNumber(String contactNumber) {
//        this.contactNumber = contactNumber;
//    }
//
//    public LocalDate getReservationDate() {
//        return reservationDate;
//    }
//
//    public void setReservationDate(LocalDate reservationDate) {
//        this.reservationDate = reservationDate;
//    }
//
//    public LocalTime getReservationTime() {
//        return reservationTime;
//    }
//
//    public void setReservationTime(LocalTime reservationTime) {
//        this.reservationTime = reservationTime;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Ophthalmology getOphthalmology() {
//        return ophthalmology;
//    }
//
//    public void setOphthalmology(Ophthalmology ophthalmology) {
//        this.ophthalmology = ophthalmology;
//    }
//
//    public Long getHospitalId() {
//        return hospitalId;
//    }
//
//    public void setHospitalId(Long hospitalId) {
//        this.hospitalId = hospitalId;
//    }
//}
package hello.login.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private String contactNumber;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ophthalmology_id")
    private Ophthalmology ophthalmology;

    private String status; // 예약 상태 필드 추가




    // 병원 운영 시간
    private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(18, 0);

    // 점심 시간
    private static final LocalTime LUNCH_START = LocalTime.of(13, 0);
    private static final LocalTime LUNCH_END = LocalTime.of(14, 0);

    // 예약 시간 단위 (15분)
    private static final int RESERVATION_INTERVAL_MINUTES = 15;

    // 예약 가능 여부 확인
    public boolean isReservable() {
        // 과거 날짜 예약 불가
        if (reservationDate.isBefore(LocalDate.now())) {
            return false;
        }

        // 운영 시간 내에 있는지 확인
        if (reservationTime.isBefore(OPENING_TIME) || reservationTime.plusMinutes(RESERVATION_INTERVAL_MINUTES).isAfter(CLOSING_TIME)) {
            return false;
        }

        // 점심 시간 내 예약 불가
        if (!reservationTime.isBefore(LUNCH_START) && reservationTime.isBefore(LUNCH_END)) {
            return false;
        }

        return true;
    }

    // 예약 시간이 중복되는지 확인
    public boolean isTimeConflict(Reservation otherReservation) {
        return this.reservationDate.equals(otherReservation.getReservationDate()) &&
                !this.reservationTime.isBefore(otherReservation.getReservationTime()) &&
                this.reservationTime.isBefore(otherReservation.getReservationTime().plusMinutes(RESERVATION_INTERVAL_MINUTES));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ophthalmology getOphthalmology() {
        return ophthalmology;
    }

    public void setOphthalmology(Ophthalmology ophthalmology) {
        this.ophthalmology = ophthalmology;
    }

}
