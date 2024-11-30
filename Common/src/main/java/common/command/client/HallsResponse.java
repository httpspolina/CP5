package common.command.client;

import common.command.SuccessResponse;
import common.model.Hall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HallsResponse implements SuccessResponse {
    private List<Hall> halls;
}
