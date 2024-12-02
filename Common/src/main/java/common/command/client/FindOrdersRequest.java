package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FindOrdersRequest implements Request {
    private Integer orderId;
}
