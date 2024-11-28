package server.db;

import common.model.Client;
import common.model.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ClientRepositoryTest {

    ClientRepository clientRepository = new ClientRepository();

    @Test
    void test() throws Exception {
        Client newClient = new Client();
        newClient.setUsername(UUID.randomUUID().toString());
        newClient.setPassword(PasswordHashing.hashPassword(UUID.randomUUID().toString()));
        newClient.setRole(Role.client);
        newClient.setName(UUID.randomUUID().toString());
        newClient.setEmail(UUID.randomUUID().toString());
        newClient.setPhone(UUID.randomUUID().toString());

        Integer newClientId = clientRepository.register(newClient);
        assertThat(newClientId).isNotNull().isPositive();

        Client foundClient = clientRepository.findByUsername(newClient.getUsername());
        assertThat(foundClient.getId()).isEqualTo(newClientId);
        assertThat(foundClient.getUsername()).isEqualTo(newClient.getUsername());
        assertThat(foundClient.getPassword()).isEqualTo(newClient.getPassword());
        assertThat(foundClient.getRole()).isEqualTo(newClient.getRole());
        assertThat(foundClient.getName()).isEqualTo(newClient.getName());
        assertThat(foundClient.getEmail()).isEqualTo(newClient.getEmail());
        assertThat(foundClient.getPhone()).isEqualTo(newClient.getPhone());
    }

}
