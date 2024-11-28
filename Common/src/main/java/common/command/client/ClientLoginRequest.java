package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class ClientLoginRequest implements Request {
    private String username;
    private String password;
}
