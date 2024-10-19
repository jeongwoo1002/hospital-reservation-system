package hello.login.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;


import lombok.Getter;
import lombok.Setter;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.time.LocalDate;
//import java.time.LocalTime;

@Getter
@Setter
//public class ReservationForm {
//    private Long id;
//    private String patientName;       // 환자 이름 추가
//    private String contactNumber;      // 연락처 추가
//
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate reservationDate; // 예약 날짜
//
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//    private LocalTime reservationTime; // 예약 시간
//
//    private Long ophthalmologyId;      // 안과 ID
//
//    @Override
//    public String toString() {
//        return "ReservationForm{" +
//                "id=" + id +
//                ", patientName='" + patientName + '\'' +
//                ", contactNumber='" + contactNumber + '\'' +
//                ", reservationDate=" + reservationDate +
//                ", reservationTime=" + reservationTime +
//                ", ophthalmologyId=" + ophthalmologyId +
//                '}';
//    }
//}
public class ReservationDto {
    private LocalDate date;
    private LocalTime time;

    // getters and setters
}
