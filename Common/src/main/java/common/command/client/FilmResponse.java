package common.command.client;

import common.command.SuccessResponse;
import common.model.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmResponse implements SuccessResponse {
    private Film film;
}
