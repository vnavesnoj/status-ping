package vnavesnoj.status_ping_controller.exception;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class WsMessageRequestException extends IllegalArgumentException {

    public WsMessageRequestException() {
        super();
    }

    public WsMessageRequestException(String s) {
        super(s);
    }

    public WsMessageRequestException(Throwable e) {
        super(e);
    }

    public WsMessageRequestException(String s, Throwable e) {
        super(s, e);
    }
}
