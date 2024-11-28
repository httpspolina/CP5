package common.command.client;

import common.command.SuccessResponse;
import common.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse implements SuccessResponse {
    private Client client;
}
