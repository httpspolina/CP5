package server.db;

import common.model.User;
import common.model.UserRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest {

    UserRepository userRepository = new UserRepository();

    @Test
    void test() throws Exception {
        User newUser = new User();
        newUser.setUsername(UUID.randomUUID().toString());
        newUser.setPassword(PasswordHashing.hashPassword(UUID.randomUUID().toString()));
        newUser.setRole(UserRole.ADMIN);

        Integer newUserId = userRepository.create(newUser);
        assertThat(newUserId).isNotNull().isPositive();

        User foundUser = userRepository.findByUsername(newUser.getUsername());
        assertThat(foundUser.getId()).isEqualTo(newUserId);
        assertThat(foundUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(foundUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(foundUser.getRole()).isEqualTo(newUser.getRole());
    }

}
