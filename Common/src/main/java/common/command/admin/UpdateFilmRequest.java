package common.command.admin;

import common.command.Request;
import common.model.Film;
import lombok.Data;

@Data
public class UpdateFilmRequest implements Request {
    private Film film;
}
