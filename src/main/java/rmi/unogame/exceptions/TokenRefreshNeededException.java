package rmi.unogame.exceptions;

public class TokenRefreshNeededException extends RuntimeException {
    public TokenRefreshNeededException(String message) {
        super(message);
    }
}
