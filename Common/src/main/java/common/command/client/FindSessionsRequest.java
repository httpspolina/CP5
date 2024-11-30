package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FindSessionsRequest implements Request {
    private Integer filmId;
    private Integer hallId;
}
