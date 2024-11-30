package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FindHallsRequest implements Request {
    private Integer filmId;
}
