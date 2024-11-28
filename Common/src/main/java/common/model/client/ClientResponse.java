package common.model.client;

import common.model.SuccessResponse;
import lombok.Data;

@Data
public class ClientResponse implements SuccessResponse {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String phone;
}
