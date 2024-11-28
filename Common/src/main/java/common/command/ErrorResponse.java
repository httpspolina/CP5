package common.command;

public interface ErrorResponse extends Response {
    ErrorResponse INSTANCE = new ErrorResponse() {
    };
}
