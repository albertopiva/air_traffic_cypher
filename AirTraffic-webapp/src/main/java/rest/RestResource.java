package rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import utils.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;

/**
 * Superclass of the REST resources
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */
public abstract class RestResource {

    /**
     * The HTTP request
     */
    protected final HttpServletRequest req;

    /**
     * The HTTP response.
     */
    protected final HttpServletResponse res;

    /**
     * The logger.
     */
    protected final Logger logger;

    protected RestResource(final HttpServletRequest req, final HttpServletResponse res) {
        this.req = req;
        this.res = res;
        this.logger = LogManager.getLogger(this.getClass());
    }

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