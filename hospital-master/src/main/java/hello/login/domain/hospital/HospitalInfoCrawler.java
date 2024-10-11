//package hello.login.domain.hospital;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import java.io.IOException;
//
//public class HospitalInfoCrawler {
//
//    public static void main(String[] args) {
//        try {
//            String url = "https://example.com"; // 크롤링할 웹 사이트 URL
//
//            // Jsoup을 사용하여 웹 페이지의 HTML 가져오기
//            Document doc = Jsoup.connect(url).get();
//
//            // 원하는 카테고리의 정보를 추출하기
//            // 안과 정보 추출
//            Elements ophthalmologyHospitals = doc.select("div.ophthalmology-hospitals > div.hospital");
//            extractAndPrintHospitalInfo(ophthalmologyHospitals, "안과");
//
//            // 대학병원 정보 추출
//            Elements universityHospitals = doc.select("div.university-hospitals > div.hospital");
//            extractAndPrintHospitalInfo(universityHospitals, "대학병원");
//
//            // 약국 정보 추출
//            Elements pharmacies = doc.select("div.pharmacies > div.pharmacy");
//            extractAndPrintPharmacyInfo(pharmacies);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 병원 정보 추출 및 출력 메서드
//    private static void extractAndPrintHospitalInfo(Elements hospitals, String category) {
//        System.out.println(category + " 정보");
//        System.out.println("-------------------------");
//        for (Element hospital : hospitals) {
//            String name = hospital.select("h2.name").text();
//            String address = hospital.select("p.address").text();
//            String hours = hospital.select("p.hours").text();
//            String phoneNumber = hospital.select("p.phone").text();
//
//            System.out.println("병원명: " + name);
//            System.out.println("주소: " + address);
//            System.out.println("진료시간: " + hours);
//            System.out.println("전화번호: " + phoneNumber);
//            System.out.println();
//        }
//    }
//
//    // 약국 정보 추출 및 출력 메서드
//    private static void extractAndPrintPharmacyInfo(Elements pharmacies) {
//        System.out.println("약국 정보");
//        System.out.println("-------------------------");
//        for (Element pharmacy : pharmacies) {
//            String name = pharmacy.select("h2.name").text();
//            String address = pharmacy.select("p.address").text();
//            String phoneNumber = pharmacy.select("p.phone").text();
//
//            System.out.println("약국명: " + name);
//            System.out.println("주소: " + address);
//            System.out.println("전화번호: " + phoneNumber);
//            System.out.println();
//        }
//    }
//}
