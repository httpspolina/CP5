package common.model.client;

import common.model.Request;
import lombok.Data;

@Data
public class ClientLoginRequest implements Request {
    private String username;
    private String password;
}
