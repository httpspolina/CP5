package common.command.supervisor;

import common.command.Response;
import common.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UsersResponse implements Response {
    private List<User> users;
}