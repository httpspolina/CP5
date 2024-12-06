package common.command.supervisor;

import common.command.Request;
import lombok.Data;

@Data
public class FindUserByIdRequest implements Request {
    private Integer userId;
}
