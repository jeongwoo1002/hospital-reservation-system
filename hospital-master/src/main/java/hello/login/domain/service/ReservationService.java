//package hello.login.domain.service;
//
//import hello.login.domain.config.auth.PrincipalDetail;
//import hello.login.domain.dto.ReservationForm;
//import hello.login.domain.model.Reservation;
//import hello.login.domain.model.User;
//import hello.login.domain.repository.OphthalmologyRepository;
//import hello.login.domain.repository.ReservationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정
//@RequiredArgsConstructor
//public class ReservationService {
//
//    private final ReservationRepository reservationRepository; // 예약 레포지토리
//    private final OphthalmologyRepository ophthalmologyRepository; // 안과 레포지토리
//
//    /**
//     * 예약 저장
//     */
//    @Transactional
//    public void saveReservation(ReservationForm reservationForm, User user) {
//        // 예약 가능한 시간인지 확인
//        if (!Reservation.isTimeSlotAvailable(reservationForm.getReservationTime(), reservationForm.getReservationDate())) {
//            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다. (평일: 09:00~18:00, 토요일: 09:00~15:00, 점심 시간 제외)");
//        }
//
//        // 이미 예약된 시간인지 확인
//        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
//            throw new IllegalArgumentException("선택한 시간은 이미 예약되었습니다.");
//        }
//
//        // 예약 정보 저장
//        Reservation reservation = new Reservation();
//        reservation.setPatientName(reservationForm.getPatientName());
//        reservation.setContactNumber(reservationForm.getContactNumber());
//        reservation.setReservationDate(reservationForm.getReservationDate());
//        reservation.setReservationTime(reservationForm.getReservationTime());
//        reservation.setUser(user);
//        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
//        reservationRepository.save(reservation);
//    }
//
//    /**
//     * 시간 슬롯이 이미 예약되었는지 확인
//     */
//    public boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
//        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTime(ophthalmologyId, date, time);
//        return !reservations.isEmpty(); // 예약이 존재하면 true 반환
//    }
//
//    /**
//     * 예약 수정
//     */
//    @Transactional
//    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
//        Reservation reservation = reservationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
//
//        // 예약 가능한 시간인지 확인
//        if (!Reservation.isTimeSlotAvailable(reservationForm.getReservationTime(), reservationForm.getReservationDate())) {
//            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다. (평일: 09:00~18:00, 토요일: 09:00~15:00, 점심 시간 제외)");
//        }
//
//        // 이미 예약된 시간인지 확인
//        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
//            throw new IllegalArgumentException("선택한 시간은 이미 예약되었습니다.");
//        }
//
//        // 예약 정보 업데이트
//        reservation.setPatientName(reservationForm.getPatientName());
//        reservation.setContactNumber(reservationForm.getContactNumber());
//        reservation.setReservationDate(reservationForm.getReservationDate());
//        reservation.setReservationTime(reservationForm.getReservationTime());
//        reservation.setUser(user);
//        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
//    }
//
//    /**
//     * 사용자의 모든 예약 조회
//     */
//    public List<Reservation> getReservationsByUser(Long userId) {
//        return reservationRepository.findByUserId(userId); // 사용자 ID로 예약 조회
//    }
//
//    /**
//     * 예약 ID로 예약 조회
//     */
//    public Reservation getReservationById(Long id) {
//        return reservationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다.")); // 예약 ID로 조회
//    }
//
//    /**
//     * 예약 삭제
//     */
//    @Transactional
//    public void deleteReservation(Long id) {
//        reservationRepository.deleteById(id); // 예약 삭제
//    }
//
//    /**
//     * 특정 날짜에 예약된 시간 조회
//     */
//    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
//        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
//        return reservations.stream()
//                .map(Reservation::getReservationTime) // 예약된 시간 리스트 반환
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 특정 날짜에 예약 가능한 시간대 반환 (예약된 시간을 제외하고 반환)
//     */
//    public List<LocalTime> getAvailableTimes(Long ophthalmologyId, LocalDate date) {
//        List<LocalTime> bookedTimes = getBookedTimes(ophthalmologyId, date); // 예약된 시간 가져오기
//        return generateTimeSlots(date).stream()
//                .filter(time -> !bookedTimes.contains(time)) // 예약된 시간 제외
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 특정 날짜의 예약 가능한 시간대 반환 (형식화된 문자열)
//     */
//    public List<String> getAvailableTimesFormatted(Long ophthalmologyId, LocalDate date) {
//        List<LocalTime> availableTimes = getAvailableTimes(ophthalmologyId, date); // 예약 가능한 시간대 가져오기
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // 시간 포맷 설정
//        return availableTimes.stream()
//                .map(time -> time.format(formatter)) // LocalTime을 문자열로 변환
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 하루의 예약 가능한 시간대 (15분 단위) 생성
//     */
//    private List<LocalTime> generateTimeSlots(LocalDate date) {
//        List<LocalTime> timeSlots = new ArrayList<>();
//
//        // 시작 및 종료 시간 설정
//        LocalTime startTime = (date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) ? LocalTime.of(9, 0) : LocalTime.of(9, 0);
//        LocalTime endTime = (date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) ? LocalTime.of(15, 0) : LocalTime.of(18, 0);
//
//        LocalTime currentTime = startTime;
//
//        while (currentTime.isBefore(endTime)) {
//            // 점심 시간(13:00 ~ 14:00) 제외
//            if (currentTime.isBefore(LocalTime.of(13, 0)) || currentTime.isAfter(LocalTime.of(14, 0))) {
//                timeSlots.add(currentTime);
//            }
//            currentTime = currentTime.plusMinutes(15); // 15분 간격으로 시간 추가
//        }
//
//        return timeSlots; // 생성된 시간 리스트 반환
//    }
//}
package hello.login.domain.service;

import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Ophthalmology;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.OphthalmologyRepository;
import hello.login.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hello.login.domain.model.Reservation.isTimeSlotAvailable;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OphthalmologyRepository ophthalmologyRepository;
    private final OphthalmologyService ophthalmologyService;

    @Transactional
    public void saveReservation(ReservationForm reservationForm, User user) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        Ophthalmology ophthalmology = ophthalmologyService.getOphthalmologyById(reservationForm.getOphthalmologyId());
        reservation.setOphthalmology(ophthalmology);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("이 예약을 수정할 권한이 없습니다.");
        }

        // 예약 시간 체크를 위한 로직 추가
        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
            throw new IllegalArgumentException("선택한 시간은 이미 예약되었습니다.");
        }

        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
    }

    @Transactional
    public void deleteReservation(Long id, User user) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("이 예약을 삭제할 권한이 없습니다.");
        }

        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
    public List<Reservation> getUserReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        System.out.println("User ID: " + userId + ", Reservations: " + reservations); // 디버깅 로그
        return reservations;
    }
//    public List<Reservation> getUserReservations(Long userId) {
//        return reservationRepository.findByUserId(userId);
//    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
    }

    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
        return reservations.stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toList());
    }

    public List<LocalTime> getAvailableTimes(Long ophthalmologyId, LocalDate date) {
        List<LocalTime> bookedTimes = getBookedTimes(ophthalmologyId, date);
        List<LocalTime> allAvailableTimes = generateTimeSlots(date);
        return allAvailableTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }

    private List<LocalTime> generateTimeSlots(LocalDate date) {
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = (date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) ? LocalTime.of(15, 0) : LocalTime.of(18, 0);

        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            if (currentTime.isBefore(LocalTime.of(13, 0)) || currentTime.isAfter(LocalTime.of(14, 0))) {
                timeSlots.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(15);
        }
        return timeSlots;
    }

    private boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
        return !reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTime(ophthalmologyId, date, time).isEmpty();
    }
}
