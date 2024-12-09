package common.command.supervisor;

import common.command.Request;
import lombok.Data;

@Data
public class FindUserByUsernameRequest implements Request {
    private String username;
}
