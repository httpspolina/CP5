package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class ClientRegisterRequest implements Request {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
}
