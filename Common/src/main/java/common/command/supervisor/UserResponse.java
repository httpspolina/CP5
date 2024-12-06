package common.command.supervisor;

import common.command.Response;
import common.model.User;
import lombok.Data;

@Data
public class UserResponse implements Response {
    private User user;
}
