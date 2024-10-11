//package hello.login.domain.crawl;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class HospitalDetailsCrawler {
//
//    public static Map<String, String> getHospitalDetails(String url) throws IOException {
//        Map<String, String> hospitalDetails = new HashMap<>();
//
//        Document doc = Jsoup.connect(url).get();
//
//        // 전화번호
//        Element phoneElement = doc.select("td:contains(전화번호) + td").first();
//        hospitalDetails.put("phone", phoneElement != null ? phoneElement.text() : "N/A");
//
//        // 홈페이지
//        Element homepageElement = doc.select("td:contains(홈페이지) + td").first();
//        hospitalDetails.put("homepage", homepageElement != null ? homepageElement.text() : "N/A");
//
//        // 주소
//        Element addressElement = doc.select("td:contains(주소) + td div").first();
//        hospitalDetails.put("address", addressElement != null ? addressElement.text() : "N/A");
//
//        // 인근 전철역
//        Element subwayElement = doc.select("td:contains(인근 전철역) + td").first();
//        hospitalDetails.put("subway", subwayElement != null ? subwayElement.text() : "N/A");
//
//        // 진료시간
//        Element hoursElement = doc.select("td:contains(진료시간) + td").first();
//        hospitalDetails.put("hours", hoursElement != null ? hoursElement.html().replace("<br>", "\n") : "N/A");
//
//        // 진료 참고사항
//        Element notesElement = doc.select("td:contains(진료 참고사항) + td").first();
//        hospitalDetails.put("notes", notesElement != null ? notesElement.html().replace("<br>", "\n") : "N/A");
//
//        return hospitalDetails;
//    }
//}
