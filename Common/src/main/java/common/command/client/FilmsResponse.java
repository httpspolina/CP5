package common.command.client;

import common.command.SuccessResponse;
import common.model.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmsResponse implements SuccessResponse {
    private List<Film> films;
}
