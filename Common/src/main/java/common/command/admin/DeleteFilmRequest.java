package common.command.admin;

import common.command.Request;
import lombok.Data;

@Data
public class DeleteFilmRequest implements Request {
    private Integer filmId;
}
