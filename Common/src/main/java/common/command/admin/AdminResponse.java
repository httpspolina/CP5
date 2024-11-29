package common.command.admin;

import common.command.SuccessResponse;
import common.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse implements SuccessResponse {
    private User user;
}
