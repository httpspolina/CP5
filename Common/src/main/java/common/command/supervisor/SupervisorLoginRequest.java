package common.command.supervisor;

import common.command.Request;
import lombok.Data;

@Data
public class SupervisorLoginRequest implements Request {
    private String username;
    private String password;
}
