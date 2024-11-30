package common.command.client;

import common.command.SuccessResponse;
import common.model.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse implements SuccessResponse {
    private Session session;
}
