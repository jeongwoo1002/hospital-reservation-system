//package hello.login;
//
//import hello.login.domain.hospital.Hospital;
//import hello.login.domain.hospital.HospitalRepository;
//import hello.login.domain.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@RequiredArgsConstructor
//public class TestDataInit {
//
//    private final HospitalRepository hospitalRepository;
//    private final MemberRepository memberRepository;
//
//    /**
//     * 테스트용 데이터 추가
//     */
//    @PostConstruct
//    public void init() {
//        hospitalRepository.save(new Hospital("최진수안과의원", "서울특별시 노원구 동일로 1379","09:30 - 18:00", "02-933-2977"));
//        hospitalRepository.save(new Hospital("상계백병원", "서울특별시 노원구 동일로 1342", "08:00 - 17:00", "02-950-1114"));
//
//        Member member = new Member();
//        member.setLoginId("test");
//        member.setPassword("test!");
//        member.setName("테스터");
//        member.setNumber("010-1238-4567"); // 전화번호 설정
//        member.setAddress("서울시 노원구");
//        member.setResident("000000-1111111"); // 주민등록번호 설정
//
//        memberRepository.save(member);
//
//    }
//
//}