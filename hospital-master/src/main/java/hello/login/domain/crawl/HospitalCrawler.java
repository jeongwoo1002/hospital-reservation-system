package hello.login.domain.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HospitalCrawler {

    public static List<Map<String, String>> getHospitalList(String url) throws IOException {
        List<Map<String, String>> hospitals = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("h1 > a");

        for (Element element : elements) {
            Map<String, String> hospital = new HashMap<>();
            hospital.put("name", element.text());
            hospital.put("detailsUrl", element.attr("href"));
            hospitals.add(hospital);
        }

        return hospitals;
    }
}
