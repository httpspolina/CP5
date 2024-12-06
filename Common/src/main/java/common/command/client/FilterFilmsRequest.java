package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class FilterFilmsRequest implements Request {
    private String filterType;
    private String sortOrder;
}