////package hello.login.domain.service;
////
////import hello.login.domain.config.auth.PrincipalDetail;
////import hello.login.domain.dto.ReservationForm;
////import hello.login.domain.model.Reservation;
////import hello.login.domain.model.User;
////import hello.login.domain.repository.OphthalmologyRepository;
////import hello.login.domain.repository.ReservationRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.core.annotation.AuthenticationPrincipal;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.time.LocalDate;
////import java.time.LocalTime;
////import java.time.format.DateTimeFormatter;
////import java.util.ArrayList;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정
////@RequiredArgsConstructor
////public class ReservationService {
////
////    private final ReservationRepository reservationRepository; // 예약 레포지토리
////    private final OphthalmologyRepository ophthalmologyRepository; // 안과 레포지토리
////
////    /**
////     * 예약 저장
////     */
////    @Transactional
////    public void saveReservation(ReservationForm reservationForm, User user) {
////        // 예약 가능한 시간인지 확인
////        if (!Reservation.isTimeSlotAvailable(reservationForm.getReservationTime(), reservationForm.getReservationDate())) {
////            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다. (평일: 09:00~18:00, 토요일: 09:00~15:00, 점심 시간 제외)");
////        }
////
////        // 이미 예약된 시간인지 확인
////        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
////            throw new IllegalArgumentException("선택한 시간은 이미 예약되었습니다.");
////        }
////
////        // 예약 정보 저장
////        Reservation reservation = new Reservation();
////        reservation.setPatientName(reservationForm.getPatientName());
////        reservation.setContactNumber(reservationForm.getContactNumber());
////        reservation.setReservationDate(reservationForm.getReservationDate());
////        reservation.setReservationTime(reservationForm.getReservationTime());
////        reservation.setUser(user);
////        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
////        reservationRepository.save(reservation);
////    }
////
////    /**
////     * 시간 슬롯이 이미 예약되었는지 확인
////     */
////    public boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
////        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTime(ophthalmologyId, date, time);
////        return !reservations.isEmpty(); // 예약이 존재하면 true 반환
////    }
////
////    /**
////     * 예약 수정
////     */
////    @Transactional
////    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
////        Reservation reservation = reservationRepository.findById(id)
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
////
////        // 예약 가능한 시간인지 확인
////        if (!Reservation.isTimeSlotAvailable(reservationForm.getReservationTime(), reservationForm.getReservationDate())) {
////            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다. (평일: 09:00~18:00, 토요일: 09:00~15:00, 점심 시간 제외)");
////        }
////
////        // 이미 예약된 시간인지 확인
////        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
////            throw new IllegalArgumentException("선택한 시간은 이미 예약되었습니다.");
////        }
////
////        // 예약 정보 업데이트
////        reservation.setPatientName(reservationForm.getPatientName());
////        reservation.setContactNumber(reservationForm.getContactNumber());
////        reservation.setReservationDate(reservationForm.getReservationDate());
////        reservation.setReservationTime(reservationForm.getReservationTime());
////        reservation.setUser(user);
////        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
////    }
////
////    /**
////     * 사용자의 모든 예약 조회
////     */
////    public List<Reservation> getReservationsByUser(Long userId) {
////        return reservationRepository.findByUserId(userId); // 사용자 ID로 예약 조회
////    }
////
////    /**
////     * 예약 ID로 예약 조회
////     */
////    public Reservation getReservationById(Long id) {
////        return reservationRepository.findById(id)
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다.")); // 예약 ID로 조회
////    }
////
////    /**
////     * 예약 삭제
////     */
////    @Transactional
////    public void deleteReservation(Long id) {
////        reservationRepository.deleteById(id); // 예약 삭제
////    }
////
////    /**
////     * 특정 날짜에 예약된 시간 조회
////     */
////    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
////        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
////        return reservations.stream()
////                .map(Reservation::getReservationTime) // 예약된 시간 리스트 반환
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * 특정 날짜에 예약 가능한 시간대 반환 (예약된 시간을 제외하고 반환)
////     */
////    public List<LocalTime> getAvailableTimes(Long ophthalmologyId, LocalDate date) {
////        List<LocalTime> bookedTimes = getBookedTimes(ophthalmologyId, date); // 예약된 시간 가져오기
////        return generateTimeSlots(date).stream()
////                .filter(time -> !bookedTimes.contains(time)) // 예약된 시간 제외
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * 특정 날짜의 예약 가능한 시간대 반환 (형식화된 문자열)
////     */
////    public List<String> getAvailableTimesFormatted(Long ophthalmologyId, LocalDate date) {
////        List<LocalTime> availableTimes = getAvailableTimes(ophthalmologyId, date); // 예약 가능한 시간대 가져오기
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // 시간 포맷 설정
////        return availableTimes.stream()
////                .map(time -> time.format(formatter)) // LocalTime을 문자열로 변환
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * 하루의 예약 가능한 시간대 (15분 단위) 생성
////     */
////    private List<LocalTime> generateTimeSlots(LocalDate date) {
////        List<LocalTime> timeSlots = new ArrayList<>();
////
////        // 시작 및 종료 시간 설정
////        LocalTime startTime = (date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) ? LocalTime.of(9, 0) : LocalTime.of(9, 0);
////        LocalTime endTime = (date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) ? LocalTime.of(15, 0) : LocalTime.of(18, 0);
////
////        LocalTime currentTime = startTime;
////
////        while (currentTime.isBefore(endTime)) {
////            // 점심 시간(13:00 ~ 14:00) 제외
////            if (currentTime.isBefore(LocalTime.of(13, 0)) || currentTime.isAfter(LocalTime.of(14, 0))) {
////                timeSlots.add(currentTime);
////            }
////            currentTime = currentTime.plusMinutes(15); // 15분 간격으로 시간 추가
////        }
////
////        return timeSlots; // 생성된 시간 리스트 반환
////    }
////}
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

    /**
     * 예약을 저장하는 메소드
     * 예약 정보와 사용자 정보를 받아서 새로운 예약을 데이터베이스에 저장합니다.
//     */
    @Transactional
    public void save(ReservationForm reservationForm, User user) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());

        if (reservationForm.getOphthalmologyId() == null) {
            throw new IllegalArgumentException("Ophthalmology ID cannot be null");
        }

        // 안과 정보를 ID를 통해 조회하여 설정
        Ophthalmology ophthalmology = ophthalmologyService.getOphthalmologyById(reservationForm.getOphthalmologyId());
        reservation.setOphthalmology(ophthalmology);

        // 예약 저장
        reservationRepository.save(reservation);
    }

//    @Transactional
//    public void saveReservation(ReservationForm reservationForm, User user) {
//        // Ophthalmology ID 검증
//        if (reservationForm.getOphthalmologyId() == null) {
//            throw new IllegalArgumentException("Ophthalmology ID cannot be null");
//        }
//
//        // 안과 정보를 ID를 통해 조회하여 설정
//        Ophthalmology ophthalmology = ophthalmologyService.getOphthalmologyById(reservationForm.getOphthalmologyId());
//
//        // Reservation 객체 생성 및 설정
//        Reservation reservation = Reservation.createReservation(
//                user.getName(), // patientName
//                user.getNumber(), // contactNumber (사용자 정보에서 가져오는 경우)
//                reservationForm.getReservationDate(),
//                reservationForm.getReservationTime(),
//                user,
//                ophthalmology // Ophthalmology 객체 전달
//        );
//
//        // Reservation 저장
//        reservationRepository.save(reservation);
//    }

    /**
     * 예약을 수정하는 메소드
     * 예약 ID와 수정할 예약 정보를 받아서, 해당 예약을 업데이트합니다.
     * 유효성 검사 및 예약 시간 중복 체크 로직도 포함되어 있습니다.
     */
    @Transactional
    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));

        // 예약한 사용자가 맞는지 확인
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("이 예약을 수정할 권한이 없습니다.");
        }

        // 예약 시간 중복 체크
        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
            throw new IllegalArgumentException("선택한 시간은 이미 예약되었습니다.");
        }

        // 예약 정보 업데이트
        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
    }

    /**
     * 예약을 삭제하는 메소드
     * 예약 ID와 사용자를 받아, 해당 사용자가 만든 예약만 삭제할 수 있도록 검증합니다.
     */
    @Transactional
    public void deleteReservation(Long id, User user) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));

        // 예약한 사용자가 맞는지 확인
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("이 예약을 삭제할 권한이 없습니다.");
        }

        reservationRepository.deleteById(id);
    }

    /**
     * 특정 사용자의 모든 예약을 조회하는 메소드
     * 사용자 ID를 받아서 해당 사용자가 예약한 모든 예약을 반환합니다.
     */
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    /**
     * 디버깅 로그와 함께 특정 사용자의 예약 목록을 조회하는 메소드
     */
    public List<Reservation> getUserReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        System.out.println("User ID: " + userId + ", Reservations: " + reservations); // 디버깅 로그
        return reservations;
    }
//    public List<Reservation> getUserReservations(Long userId) {
//        return reservationRepository.findByUserId(userId);
//    }


    /**
     * 예약 ID를 통해 특정 예약을 조회하는 메소드
     */
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
    }

    /**
     * 특정 날짜에 예약된 시간을 조회하는 메소드
     * 해당 날짜에 이미 예약된 시간 목록을 반환합니다.
     */
    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
        return reservations.stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toList());
    }
    /**
     * 특정 날짜에 예약 가능한 시간을 조회하는 메소드
     * 이미 예약된 시간을 제외하고 반환합니다.
     */
    public List<LocalTime> getAvailableTimes(Long ophthalmologyId, LocalDate date) {
        List<LocalTime> bookedTimes = getBookedTimes(ophthalmologyId, date);
        List<LocalTime> allAvailableTimes = generateTimeSlots(date);
        return allAvailableTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }
    /**
     * 주어진 날짜에 따라 시간대를 생성하는 메소드
     * 평일과 토요일의 운영 시간을 기준으로 15분 간격의 예약 가능 시간 목록을 반환합니다.
     */
    private List<LocalTime> generateTimeSlots(LocalDate date) {
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = (date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) ? LocalTime.of(15, 0) : LocalTime.of(18, 0);

        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            // 점심 시간 제외 (13:00 ~ 14:00)
            if (currentTime.isBefore(LocalTime.of(13, 0)) || currentTime.isAfter(LocalTime.of(14, 0))) {
                timeSlots.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(15);  // 15분 간격 추가
        }
        return timeSlots;
    }

    /**
     * 예약된 시간을 확인하는 메소드
     * 특정 날짜와 시간에 이미 예약된 경우 true를 반환합니다.
     */
    private boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
        return !reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTime(ophthalmologyId, date, time).isEmpty();
    }
}
