package utils;

import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Contains the enumeration of the error code needed in the servlets.
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */
public enum ErrorCode {


    /**
     * Country not found.
     */
    COUNTRY_NOT_FOUND(-116, HttpServletResponse.SC_NOT_FOUND, "Country not found."),


    /**
     * Operation unknown.
     */
    OPERATION_UNKNOWN(-200, HttpServletResponse.SC_BAD_REQUEST, "Operation unknown."),

    /**
     * Method of the request not allowed.
     */
    METHOD_NOT_ALLOWED(500, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "The method is not allowed"),

    /**
     * Internal Error.
     */
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
;
    /**
     * My own code of the error.
     */
    private final int errorCode;

    /**
     * The HTTP code of the error.
     */
    private final int httpCode;

    /**
     * The error message.
     */
    private final String errorMessage;

    /**
     * The constructore of the ErrorCode.
     *
     * @param errorCode my own code of the error.
     * @param httpCode the HTTP code of the error.
     * @param errorMessage the error message.
     */
    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Get my own error code.
     *
     * @return my own error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Get the HTTP error code.
     *
     * @return the HTTP error code.
     */
    public int getHTTPCode() {
        return httpCode;
    }


    /**
     * Get the message of the error.
     *
     * @return the message of the error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Create a JSON Object of the ErrorCode.
     *
     * @return JSONObject of the ErrorCode.
     */
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }
}