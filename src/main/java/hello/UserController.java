package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.model.User;
import hello.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/init")
	private void init() throws Exception {
		userRepository.save(new User("1", "ichiro"));
		userRepository.save(new User("2", "jiro"));
		userRepository.save(new User("3", "saburo"));
		userRepository.save(new User("4", "shiro"));
		userRepository.save(new User("5", "goro"));
	}

}
