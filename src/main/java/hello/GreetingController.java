package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hello.model.User;
import hello.repositories.UserRepository;
import java.util.Optional;
import com.amazonaws.xray.AWSXRay;


@Controller
public class GreetingController {
	@Autowired
	private UserRepository userRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="id", required=false, defaultValue="1") String id, Model model) {
        
        AWSXRay.beginSubsegment("test-sleep");
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
            
        }
        finally {
            AWSXRay.endSubsegment();
        }
        
        User user = userRepository.findById(id).get();
        model.addAttribute("name", user.getName());
        return "greeting";
    }

}