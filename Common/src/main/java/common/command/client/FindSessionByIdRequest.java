package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FindSessionByIdRequest implements Request {
    private Integer sessionId;
}
