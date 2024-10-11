//package hello.login.domain.crawl;
//
//import hello.login.domain.model.Ophthalmology;
//import hello.login.domain.repository.OphthalmologyRepository;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
////
////@Service
////public class OphthalmologyCrawlService {
////    private static final String OphthalmologyURL = "https://augenkorea.co.kr/index/bbs/board.php?bo_table=s4_3&page=%d";
////    private static final int MAX_PAGES = 102; // 원하는 페이지 수로 변경
////
////
////    @PostConstruct
////    public void getOphthalmology() throws IOException {
////        for (int pageNum = 1; pageNum <= MAX_PAGES; pageNum++) {
////            String url = String.format(OphthalmologyURL, pageNum);
////            Document doc = Jsoup.connect(url).get();
////            Elements contents = doc.select(".div_tb_tr");
////
////            for (Element content : contents) {
////                Element nameElement = content.select(".col_subject a").first();
////                Element addressElement = content.select(".col_writer").first();
////                Element numberElement = content.select(".col_date").first();
////
////                String name = (nameElement != null) ? nameElement.text() : "";
////                String address = (addressElement != null) ? addressElement.text() : "";
////                String number = (numberElement != null) ? numberElement.text() : "";
////
////                Ophthalmology ophthalmology = Ophthalmology.builder()
////                        .name(name)
////                        .address(address)
////                        .number(number)
////                        .build();
////
////                System.out.println(ophthalmology.toString());
////            }
////        }
////    }
////}
//
//
//@Service
//public class OphthalmologyCrawlService {
//    private static final String OPHTHALMOLOGY_URL = "https://augenkorea.co.kr/index/bbs/board.php?bo_table=s4_3&page=%d";
//    private static final int MAX_PAGES = 102; // 원하는 페이지 수로 변경
//
//    private final OphthalmologyRepository ophthalmologyRepository;
//
//    @Autowired
//    public OphthalmologyCrawlService(OphthalmologyRepository ophthalmologyRepository) {
//        this.ophthalmologyRepository = ophthalmologyRepository;
//    }
//
//    @PostConstruct
//    public void crawlOphthalmology() throws IOException {
//        for (int pageNum = 1; pageNum <= MAX_PAGES; pageNum++) {
//            String url = String.format(OPHTHALMOLOGY_URL, pageNum);
//            Document doc = Jsoup.connect(url).get();
//            Elements contents = doc.select(".div_tb_tr");
//
//            for (Element content : contents) {
//                Element nameElement = content.select(".col_subject a").first();
//                Element addressElement = content.select(".col_writer").first();
//                Element numberElement = content.select(".col_date").first();
//
//                String name = (nameElement != null) ? nameElement.text() : "";
//                String address = (addressElement != null) ? addressElement.text() : "";
//                String number = (numberElement != null) ? numberElement.text() : "";
//
//                Ophthalmology ophthalmology = Ophthalmology.builder()
//                        .name(name)
//                        .address(address)
//                        .number(number)
//                        .build();
//
//                // 데이터베이스에 저장
//                ophthalmologyRepository.save(ophthalmology);
//
//                System.out.println(ophthalmology.toString());
//            }
//        }
//    }
//}
package hello.login.domain.crawl;

import hello.login.domain.model.Ophthalmology;
import hello.login.domain.repository.OphthalmologyRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OphthalmologyCrawlService {

    private static final Logger logger = LoggerFactory.getLogger(OphthalmologyCrawlService.class);

    private static final String OPHTHALMOLOGY_URL = "https://augenkorea.co.kr/index/bbs/board.php?bo_table=s4_3&page=%d";
    private static final int MAX_PAGES = 102; // 원하는 페이지 수로 변경

    private final OphthalmologyRepository ophthalmologyRepository;

    @Autowired
    public OphthalmologyCrawlService(OphthalmologyRepository ophthalmologyRepository) {
        this.ophthalmologyRepository = ophthalmologyRepository;
    }

    @PostConstruct
    public void crawlOphthalmology() {
        Set<String> uniqueEntries = new HashSet<>();
        try {
            for (int pageNum = 1; pageNum <= MAX_PAGES; pageNum++) {
                String url = String.format(OPHTHALMOLOGY_URL, pageNum);
                Document doc = Jsoup.connect(url).get();
                Elements contents = doc.select(".div_tb_tr");

                for (Element content : contents) {
                    Element nameElement = content.select(".col_subject a").first();
                    Element addressElement = content.select(".col_writer").first();
                    Element numberElement = content.select(".col_date").first();

                    String name = (nameElement != null) ? nameElement.text() : "";
                    String address = (addressElement != null) ? addressElement.text() : "";
                    String number = (numberElement != null) ? numberElement.text() : "";

                    if (!uniqueEntries.add(name + address)) {
                        logger.info("Skipping duplicate entry: {} - {}", name, address);
                        continue;
                    }

                    Ophthalmology ophthalmology = Ophthalmology.builder()
                            .name(name)
                            .address(address)
                            .number(number)
                            .build();

                    // 데이터베이스에 저장
                    ophthalmologyRepository.save(ophthalmology);

                    logger.info("Saved ophthalmology: {}", ophthalmology);
                }
            }
        } catch (IOException e) {
            logger.error("Error during ophthalmology crawl", e);
        }
    }
}
