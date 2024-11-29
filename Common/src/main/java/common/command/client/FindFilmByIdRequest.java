package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FindFilmByIdRequest implements Request {
    private Integer filmId;
}
