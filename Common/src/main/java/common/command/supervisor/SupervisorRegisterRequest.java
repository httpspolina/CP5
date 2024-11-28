package common.command.supervisor;

import common.command.Request;
import lombok.Data;

@Data
public class SupervisorRegisterRequest implements Request {
    private String username;
    private String password;
    private String code;
}
