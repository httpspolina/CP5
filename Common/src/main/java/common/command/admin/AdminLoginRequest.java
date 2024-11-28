package common.command.admin;

import common.command.Request;
import lombok.Data;

@Data
public class AdminLoginRequest implements Request {
    private String username;
    private String password;
}
