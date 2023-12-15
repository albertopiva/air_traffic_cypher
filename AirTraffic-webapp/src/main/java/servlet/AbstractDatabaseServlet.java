package servlet;
import utils.ErrorCode;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Abstract database servlet
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractDatabaseServlet extends HttpServlet {


    /**
     * Write on the Output Streaming of the HTTP Response an {@code ErrorCode}.
     *
     * @param res the HTTP response.
     * @param ec the {@code ErrorCode} to write in the streaming output.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void writeError(HttpServletResponse res, ErrorCode ec) throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(ec.toJSON().toString());
    }
}
