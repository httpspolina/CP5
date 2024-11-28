package common.model.admin;

import common.model.Request;
import lombok.Data;

@Data
public class AdminLoginRequest implements Request {
    private String username;
    private String password;
}
