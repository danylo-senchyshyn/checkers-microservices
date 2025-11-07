package sk.tuke.gamestudio.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.gamestudio.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByNickname(String nickname);
}