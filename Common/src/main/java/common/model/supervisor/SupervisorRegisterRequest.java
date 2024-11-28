package common.model.supervisor;

import common.model.Request;
import lombok.Data;

@Data
public class SupervisorRegisterRequest implements Request {
    private String username;
    private String password;
    private String code;
}
