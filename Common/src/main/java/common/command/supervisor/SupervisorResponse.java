package common.command.supervisor;

import common.command.SuccessResponse;
import common.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorResponse implements SuccessResponse {
    private User user;
}
