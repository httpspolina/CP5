package common.command.client;

import common.command.SuccessResponse;
import common.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponse implements SuccessResponse {
    private List<Order> orders = new ArrayList<>();
}
