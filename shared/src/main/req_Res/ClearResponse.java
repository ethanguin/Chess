package req_Res;

/**
 * <code>ClearResponse</code> a response object that holds data relevant to the HTTP handlers for clearing the database
 */
public class ClearResponse {
    /**
     * the error message that is returned if response is a failure response
     */
    private String message;

    public ClearResponse() {
    }

    public ClearResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
