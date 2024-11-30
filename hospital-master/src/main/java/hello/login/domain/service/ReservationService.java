package hello.login.domain.service;

import hello.login.domain.model.Reservation;
import hello.login.domain.repository.ReservationRepository;
import org.springframework.stereotype.Service;


import hello.login.domain.dto.ReservationForm;
import hello.login.domain.model.Ophthalmology;
import hello.login.domain.model.User;
import hello.login.domain.repository.OphthalmologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OphthalmologyRepository ophthalmologyRepository;
    private final OphthalmologyService ophthalmologyService;


    public Reservation createReservation(ReservationForm reservationForm, User user) {
        // Ophthalmology ID와 User가 null이 아닌지 확인
        if (reservationForm.getOphthalmologyId() == null) {
            throw new IllegalArgumentException("Ophthalmology ID는 null일 수 없습니다.");
        }

        if (user == null) {
            throw new IllegalArgumentException("User ID는 null일 수 없습니다.");
        }

        // Ophthalmology ID로 안과 정보 조회
        Ophthalmology ophthalmology = ophthalmologyRepository.findById(reservationForm.getOphthalmologyId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Ophthalmology ID입니다."));

        // 예약 객체 생성
        Reservation reservation = new Reservation();
        reservation.setOphthalmology(ophthalmology);
        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        reservation.setUser(user);

        // 예약 저장
        return reservationRepository.save(reservation);
    }


    /**
     * 예약을 저장하는 메소드
     * 예약 정보와 사용자 정보를 받아서 새로운 예약을 데이터베이스에 저장합니다.
//     */
    @Transactional
    public Reservation save(ReservationForm reservationForm, User user) {
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
        return reservationRepository.save(reservation); // 저장 후 예약 객체 반환
    }

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
