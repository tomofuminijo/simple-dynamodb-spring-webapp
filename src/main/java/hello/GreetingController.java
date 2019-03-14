package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hello.model.Greeting;
import hello.repositories.GreetingRepository;
import java.util.Optional;
import com.amazonaws.xray.AWSXRay;


@Controller
public class GreetingController {
	@Autowired
	private GreetingRepository greetingRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="lang", required=false, defaultValue="en") String lang, Model model) {
        
        AWSXRay.beginSubsegment("test-sleep");
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
            
        }
        finally {
            AWSXRay.endSubsegment();
        }
        
        Greeting greeting = greetingRepository.findById(lang).get();
        model.addAttribute("hello", greeting.getHello());
        model.addAttribute("lang", lang);
        return "greeting";
    }


	@GetMapping("/init")
	private String init() throws Exception {
		greetingRepository.save(new Greeting("en", "Hello"));
		greetingRepository.save(new Greeting("ja", "こんにちは"));
		greetingRepository.save(new Greeting("zh", "您好"));
		greetingRepository.save(new Greeting("ko", "여보세요"));
		greetingRepository.save(new Greeting("el", "Saluton"));
		greetingRepository.save(new Greeting("it", "Ciao"));
		greetingRepository.save(new Greeting("de", "Hallo"));
		greetingRepository.save(new Greeting("fr", "Bonjour"));
		return "index";
	}

}