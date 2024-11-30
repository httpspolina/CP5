package common.command.client;

import common.command.SuccessResponse;
import common.model.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionsResponse implements SuccessResponse {
    private List<Session> sessions = new ArrayList<>();
}
