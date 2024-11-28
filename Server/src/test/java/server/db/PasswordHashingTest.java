package server.db;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordHashingTest {

    @Test
    void test() throws Exception {
        String plainPassword1 = UUID.randomUUID().toString();
        String hashedPassword1 = PasswordHashing.hashPassword(plainPassword1);
        String hashedPassword2 = PasswordHashing.hashPassword(plainPassword1);
        assertThat(hashedPassword1).isNotEqualTo(hashedPassword2);
        assertThat(PasswordHashing.verifyPassword(plainPassword1, hashedPassword1)).isTrue();

        String plainPassword2 = UUID.randomUUID().toString();
        assertThat(PasswordHashing.verifyPassword(plainPassword2, hashedPassword1)).isFalse();
    }

}
