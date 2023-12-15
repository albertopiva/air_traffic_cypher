package servlet;

import rest.FlightRest;
import utils.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Servlet that manage the rest call
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */
public class RestDispatcherServlet extends AbstractDatabaseServlet{


    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String op = req.getRequestURI();
        try {

            if (processFlights(req, res)) {
                return;
            }

            writeError(res, ErrorCode.OPERATION_UNKNOWN);

        }catch(Exception s){

            //writeError(res,ErrorCode.INTERNAL_ERROR);
            res.getOutputStream().println(s.getMessage());
        }
    }

    private boolean processFlights(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String op = req.getRequestURI();
        //res.getOutputStream().println("op: "+op);
        if(!op.contains("flights"))
            return false;
        op = op.substring(op.lastIndexOf("flights"));
        String[] tokens = op.split("/");
        //the first token will always be "conference";
        //the second can be "view" or "manage";

        if(tokens[1].equals("country")){

            //res.getOutputStream().println("op: "+op);
            switch (req.getMethod()) {
                case "GET":
                    new FlightRest(req,res).getCountryOut(tokens[2]);
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("airports")){

            //res.getOutputStream().println("op: "+op);
            switch (req.getMethod()) {
                case "GET":
                    new FlightRest(req,res).getAirportToCountry(tokens[2],tokens[3]);
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("fir")){

            //res.getOutputStream().println("op: "+op);
            switch (req.getMethod()) {
                case "GET":
                    new FlightRest(req,res).getFirToAirport(tokens[2],tokens[3]);
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else{
            return false;
        }
        return true;
    }
    private boolean processAirspaces(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String op = req.getRequestURI();
        //res.getOutputStream().println("op: "+op);
        if(!op.contains("airspaces"))
            return false;
        op = op.substring(op.lastIndexOf("airspaces"));
        String[] tokens = op.split("/");
        //the first token will always be "conference";
        //the second can be "view" or "manage";

        if(tokens.length == 2){

            //res.getOutputStream().println("op: "+op);
            switch (req.getMethod()) {
                case "GET":
                    new FlightRest(req,res).getCountryOut(tokens[1]);
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("airports")){

            //res.getOutputStream().println("op: "+op);
            switch (req.getMethod()) {
                case "GET":
                    new FlightRest(req,res).getAirportToCountry(tokens[2],tokens[3]);
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else{
            return false;
        }
        return true;
    }
}
