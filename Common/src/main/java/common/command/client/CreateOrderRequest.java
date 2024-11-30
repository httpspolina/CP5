package common.command.client;

import common.command.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest implements Request {
    private Integer sessionId;
    private Integer paymentMethodId;
    private Set<Integer> selectedSeatIndexes = new HashSet<>();
}
