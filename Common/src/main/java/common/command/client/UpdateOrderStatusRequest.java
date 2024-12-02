package common.command.client;

import common.command.Request;
import common.model.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest implements Request {
    private Integer orderId;
    private OrderStatus status;
}
