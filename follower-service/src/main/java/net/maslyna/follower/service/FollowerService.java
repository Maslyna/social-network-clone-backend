package net.maslyna.follower.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.follower.exception.UserAlreadyExists;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FollowerService {
    private final UserRepository userRepository;

    public void userRegistration(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new UserAlreadyExists(
                    HttpStatus.CONFLICT,
                    "error.user.already-exists"
            );
        }
        userRepository.save(
                User.builder()
                        .id(userId)
                        .build()
        );
        log.info("user with id = {} was saved", userId);
    }


    //TODO: implement methods
    public Page<User> getUserFollowers() {
        return null;
    }

    public Page<User> getUserSubscriptions() {
        return null;
    }
}
