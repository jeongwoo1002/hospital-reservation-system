//package hello.login.domain.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@Slf4j
//public class HomeController {
//
//        @GetMapping("/")
//        public String Home() {
//            return "home";
//        }
//}
package hello.login.domain.controller;

import hello.login.domain.model.Ophthalmology;
import hello.login.domain.repository.OphthalmologyRepository;
import hello.login.domain.service.OphthalmologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final OphthalmologyService ophthalmologyService;
    private final OphthalmologyRepository ophthalmologyRepository;

    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Double lat,
                       @RequestParam(required = false) Double lng) {
        int pageSize = 10; // 한 페이지에 보여줄 항목 수
        Page<Ophthalmology> ophthalmologyPage;
        if (lat != null && lng != null) {
            List<Ophthalmology> nearbyOphthalmologies = ophthalmologyService.findNearbyOphthalmologies(lat, lng);
            model.addAttribute("ophthalmologies", nearbyOphthalmologies);
        } else if (keyword != null && !keyword.isEmpty()) {
            ophthalmologyPage = ophthalmologyService.searchByAddress(keyword, PageRequest.of(page, pageSize));
            model.addAttribute("ophthalmologyPage", ophthalmologyPage);
        } else {
            ophthalmologyPage = ophthalmologyService.getAllOphthalmologies(PageRequest.of(page, pageSize));
            model.addAttribute("ophthalmologyPage", ophthalmologyPage);
        }
        model.addAttribute("keyword", keyword);
        return "home";
    }
}
