//package hello.login.domain.crawl;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class Main {
//    public static void main(String[] args) {
//        String url = "병원 목록 페이지 URL";
//
//        try {
//            List<Map<String, String>> hospitalList = HospitalCrawler.getHospitalList(url);
//            List<Map<String, String>> detailedHospitalList = new ArrayList<>();
//
//            for (Map<String, String> hospital : hospitalList) {
//                String detailsUrl = hospital.get("detailsUrl");
//                Map<String, String> details = HospitalDetailsCrawler.getHospitalDetails(detailsUrl);
//                hospital.putAll(details);
//                detailedHospitalList.add(hospital);
//            }
//
//            // 결과 출력 (또는 파일 저장 등)
//            for (Map<String, String> hospital : detailedHospitalList) {
//                System.out.println("병원명: " + hospital.get("name"));
//                System.out.println("전화번호: " + hospital.get("phone"));
//                System.out.println("홈페이지: " + hospital.get("homepage"));
//                System.out.println("주소: " + hospital.get("address"));
//                System.out.println("인근 전철역: " + hospital.get("subway"));
//                System.out.println("진료시간: " + hospital.get("hours"));
//                System.out.println("진료 참고사항: " + hospital.get("notes"));
//                System.out.println("-----------------------------");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
