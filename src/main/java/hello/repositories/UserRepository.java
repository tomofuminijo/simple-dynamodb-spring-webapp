package hello.repositories;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import hello.model.User;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
//	Optional<User> findById(String id);
}
