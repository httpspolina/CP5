package common.command.client;

import common.command.SuccessResponse;
import common.model.Order;
import common.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindOrdersResponse implements SuccessResponse {
    private List<Order> orders = new ArrayList<>();
}