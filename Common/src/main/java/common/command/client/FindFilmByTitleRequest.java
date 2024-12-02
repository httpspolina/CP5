package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FindFilmByTitleRequest implements Request {
    private String title;
}
