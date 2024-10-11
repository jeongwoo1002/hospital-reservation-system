//package hello.login.domain.service;
//
//import hello.login.domain.dto.ReservationForm;
//import hello.login.domain.model.Reservation;
//import hello.login.domain.model.User;
//import hello.login.domain.repository.OphthalmologyRepository;
//import hello.login.domain.repository.ReservationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class ReservationService {
//
//    private final ReservationRepository reservationRepository;
//    private final OphthalmologyRepository ophthalmologyRepository;
//
//    // 예약 저장
//    @Transactional
//    public void saveReservation(ReservationForm reservationForm, User user) {
//        // 선택한 시간 슬롯이 이미 예약되었는지 확인
//        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
//            throw new IllegalArgumentException("선택한 시간 슬롯은 이미 예약되었습니다.");
//        }
//
//        // 예약 정보 생성 및 저장
//        Reservation reservation = new Reservation();
//        reservation.setPatientName(reservationForm.getPatientName());
//        reservation.setContactNumber(reservationForm.getContactNumber());
//        reservation.setReservationDate(reservationForm.getReservationDate());
//        reservation.setReservationTime(reservationForm.getReservationTime());
//        reservation.setUser(user);
//        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
//        reservation.setStatus("예약됨"); // 기본 상태 설정
//        reservationRepository.save(reservation);
//    }
//
//    // 선택한 시간 슬롯이 예약되었는지 확인
//    public boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
//        // 15분 단위로 예약이 겹치는지 확인
//        LocalTime startTime = time;
//        LocalTime endTime = time.plusMinutes(15);
//
//        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTimeBetween(
//                ophthalmologyId, date, startTime, endTime);
//        return !reservations.isEmpty();
//    }
//
//    // 예약 업데이트
//    @Transactional
//    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
//        Reservation reservation = reservationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
//        reservation.setPatientName(reservationForm.getPatientName());
//        reservation.setContactNumber(reservationForm.getContactNumber());
//        reservation.setReservationDate(reservationForm.getReservationDate());
//        reservation.setReservationTime(reservationForm.getReservationTime());
//        reservation.setUser(user);
//        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
//        reservation.setStatus("예약됨"); // 예약 상태 유지
//    }
//
//    // 사용자별 예약 목록 조회
//    public List<Reservation> getReservationsByUser(Long userId) {
//        return reservationRepository.findByUserId(userId);
//    }
//
//    // 예약 ID로 예약 조회
//    public Reservation getReservationById(Long id) {
//        return reservationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
//    }
//
//    // 예약 삭제
//    @Transactional
//    public void deleteReservation(Long id) {
//        reservationRepository.deleteById(id);
//    }
//
//    // 특정 날짜의 예약된 시간 목록 조회
//    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
//        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
//        return reservations.stream()
//                .map(Reservation::getReservationTime)
//                .collect(Collectors.toList());
//    }
//
//    // 병원 관리자가 관리하는 예약 목록을 반환합니다.
//    public List<Reservation> getReservationsByHospitalUser(Long hospitalUserId) {
//        return reservationRepository.findReservationsByHospitalUserId(hospitalUserId);
//    }
//
//    // 병원 ID와 날짜에 맞는 예약 가능한 시간을 반환하는 로직
//    public List<String> getAvailableTimes(Long ophthalmologyId, LocalDate date) {
//        List<LocalTime> reservedTimes = reservationRepository.findReservedTimesByOphthalmologyAndDate(ophthalmologyId, date);
//        List<String> allPossibleTimes = generateTimeSlots();
//        return allPossibleTimes.stream()
//                .filter(time -> !reservedTimes.contains(LocalTime.parse(time)))
//                .collect(Collectors.toList());
//    }
//
//    // 예약 가능한 시간 슬롯을 생성하는 메서드
//    private List<String> generateTimeSlots() {
//        List<String> timeSlots = new ArrayList<>();
//        LocalTime startTime = LocalTime.of(9, 0);
//        LocalTime endTime = LocalTime.of(18, 0);
//        while (!startTime.isAfter(endTime)) {
//            timeSlots.add(startTime.toString());
//            startTime = startTime.plusHours(1);  // 1시간 단위로 시간 슬롯 생성
//        }
//        return timeSlots;
//    }
//
//    // 병원 ID와 날짜에 대해 예약 가능한 날짜 목록을 반환하는 메서드
//    public List<LocalDate> getAvailableDates(Long ophthalmologyId) {
//        // 여기에 예약 가능한 날짜를 계산하여 반환하는 로직을 작성합니다.
//        // 예를 들어, 오늘부터 30일 이내의 날짜를 반환할 수 있습니다.
//        LocalDate today = LocalDate.now();
//        return today.datesUntil(today.plusDays(30)).collect(Collectors.toList());
//    }
//}
//
////@Service
////@Transactional(readOnly = true)
////@RequiredArgsConstructor
////public class ReservationService {
////
////    private final ReservationRepository reservationRepository;
////    private final OphthalmologyRepository ophthalmologyRepository;
////
////    @Transactional
////    public void saveReservation(ReservationForm reservationForm, User user) {
////        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
////            throw new IllegalArgumentException("선택한 시간 슬롯은 이미 예약되었습니다.");
////        }
////
////        Reservation reservation = new Reservation();
////        reservation.setPatientName(reservationForm.getPatientName());
////        reservation.setContactNumber(reservationForm.getContactNumber());
////        reservation.setReservationDate(reservationForm.getReservationDate());
////        reservation.setReservationTime(reservationForm.getReservationTime());
////        reservation.setUser(user);
////        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
////        reservation.setStatus("예약됨");
////        reservationRepository.save(reservation);
////    }
////
////    public boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
////        LocalTime startTime = time;
////        LocalTime endTime = time.plusMinutes(15);
////
////        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTimeBetween(
////                ophthalmologyId, date, startTime, endTime);
////        return !reservations.isEmpty();
////    }
////
////    @Transactional
////    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
////        Reservation reservation = reservationRepository.findById(id)
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
////        reservation.setPatientName(reservationForm.getPatientName());
////        reservation.setContactNumber(reservationForm.getContactNumber());
////        reservation.setReservationDate(reservationForm.getReservationDate());
////        reservation.setReservationTime(reservationForm.getReservationTime());
////        reservation.setUser(user);
////        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
////        reservation.setStatus("예약됨");
////    }
////
////    public List<Reservation> getReservationsByUser(Long userId) {
////        return reservationRepository.findByUserId(userId);
////    }
////
////    public Reservation getReservationById(Long id) {
////        return reservationRepository.findById(id)
////                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
////    }
////
////    @Transactional
////    public void deleteReservation(Long id) {
////        reservationRepository.deleteById(id);
////    }
////
////    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
////        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
////        return reservations.stream()
////                .map(Reservation::getReservationTime)
////                .collect(Collectors.toList());
////    }
////
////    public List<String> getAvailableTimes(Long ophthalmologyId, LocalDate date) {
////        List<LocalTime> reservedTimes = reservationRepository.findReservedTimesByOphthalmologyAndDate(ophthalmologyId, date);
////        List<String> allPossibleTimes = generateTimeSlots();
////        return allPossibleTimes.stream()
////                .filter(time -> !reservedTimes.contains(LocalTime.parse(time)))
////                .collect(Collectors.toList());
////    }
////
////    private List<String> generateTimeSlots() {
////        List<String> timeSlots = new ArrayList<>();
////        LocalTime startTime = LocalTime.of(9, 0);
////        LocalTime endTime = LocalTime.of(18, 0);
////        LocalTime lunchStart = LocalTime.of(13, 0);
////        LocalTime lunchEnd = LocalTime.of(14, 0);
////
////        while (!startTime.isAfter(endTime)) {
////            if (startTime.isBefore(lunchStart) || startTime.isAfter(lunchEnd)) {
////                timeSlots.add(startTime.toString());
////            }
////            startTime = startTime.plusMinutes(15);
////        }
////        return timeSlots;
////    }
////
////    public List<LocalDate> getAvailableDates(Long ophthalmologyId) {
////        LocalDate today = LocalDate.now();
////        return today.datesUntil(today.plusDays(30)).collect(Collectors.toList());
////    }
////
////    public List<Reservation> getReservationsByHospitalUser(Long hospitalUserId) {
////        return reservationRepository.findReservationsByHospitalUserId(hospitalUserId);
////    }
////}

package hello.login.domain.service;

import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Reservation;
import hello.login.domain.model.User;
import hello.login.domain.repository.OphthalmologyRepository;
import hello.login.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//public class ReservationService {
//
//    private final ReservationRepository reservationRepository;
//    private final OphthalmologyRepository ophthalmologyRepository;
//
//    @Transactional
//    public void saveReservation(ReservationForm reservationForm, User user) {
//        Reservation reservation = new Reservation();
//        reservation.setPatientName(reservationForm.getPatientName());
//        reservation.setContactNumber(reservationForm.getContactNumber());
//        reservation.setReservationTime(reservationForm.getReservationTime());
//        reservation.setUser(user);
//        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Ophthalmology ID")));
//        reservationRepository.save(reservation);
//    }
//
//    @Transactional
//    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
//        Reservation reservation = reservationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation ID"));
//        reservation.setPatientName(reservationForm.getPatientName());
//        reservation.setContactNumber(reservationForm.getContactNumber());
//        reservation.setReservationTime(reservationForm.getReservationTime());
//        reservation.setUser(user);
//        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Ophthalmology ID")));
//    }
//
//    public List<Reservation> getReservationsByUser(Long userId) {
//        return reservationRepository.findByUserId(userId);
//    }
//
//    public Reservation getReservationById(Long id) {
//        return reservationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Reservation ID"));
//    }
//
//    @Transactional
//    public void deleteReservation(Long id) {
//        reservationRepository.deleteById(id);
//    }
//}
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OphthalmologyRepository ophthalmologyRepository;

    @Transactional
    public void saveReservation(ReservationForm reservationForm, User user) {
        // 이미 예약된 시간 슬롯인지 확인
        if (isTimeSlotBooked(reservationForm.getOphthalmologyId(), reservationForm.getReservationDate(), reservationForm.getReservationTime())) {
            throw new IllegalArgumentException("선택한 시간 슬롯은 이미 예약되었습니다.");
        }

        Reservation reservation = new Reservation();
        reservation.setPatientName(reservationForm.getPatientName());
        reservation.setContactNumber(reservationForm.getContactNumber());
        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        reservation.setUser(user);
        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
        reservationRepository.save(reservation);
    }

    public boolean isTimeSlotBooked(Long ophthalmologyId, LocalDate date, LocalTime time) {
        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDateAndReservationTime(ophthalmologyId, date, time);
        return !reservations.isEmpty();
    }

    @Transactional
    public void updateReservation(Long id, ReservationForm reservationForm, User user) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
        reservation.setPatientName(reservationForm.getPatientName());
        reservation.setContactNumber(reservationForm.getContactNumber());
        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        reservation.setUser(user);
        reservation.setOphthalmology(ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 안과 ID입니다.")));
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 ID입니다."));
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<LocalTime> getBookedTimes(Long ophthalmologyId, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByOphthalmologyIdAndReservationDate(ophthalmologyId, date);
        return reservations.stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toList());
    }
}