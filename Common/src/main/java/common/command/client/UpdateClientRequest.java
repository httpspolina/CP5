package common.command.client;

import common.command.Request;
import common.model.Client;
import lombok.Data;

@Data
public class UpdateClientRequest implements Request {
    private Client client;
}
