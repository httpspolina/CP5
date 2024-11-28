package common.model.supervisor;

import common.model.Request;
import lombok.Data;

@Data
public class SupervisorLoginRequest implements Request {
    private String username;
    private String password;
}
