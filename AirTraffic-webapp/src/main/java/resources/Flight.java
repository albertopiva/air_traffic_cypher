/**
 * Resource that represents a Flight
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */

package resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;


public class Flight extends Resource{

    final private String id;
    final private String airline;
    final private String airportDep;
    final private String airportArr;
    final private ZonedDateTime timeDep;
    final private ZonedDateTime timeArr;


    /**
     * Constructor with paramters
     * @param id the id of the flight
     * @param airline the airline that performed the flight
     * @param airportDep the departure airport
     * @param airportArr the arrival airport
     * @param timeDep the zoned departure time
     * @param timeArr the zoned arrival time
     */
    public Flight(String id, String airline, String airportDep, String airportArr, ZonedDateTime timeDep, ZonedDateTime timeArr) {
        this.id = id;
        this.airline = airline;
        this.airportDep = airportDep;
        this.airportArr = airportArr;
        this.timeDep = timeDep;
        this.timeArr = timeArr;
    }

    /**
     * Return the flight id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Return the airline company's name
     * @return
     */
    public String getAirline() {
        return airline;
    }

    /**
     * Return the departure airport
     * @return
     */
    public String getAirportDep() {
        return airportDep;
    }

    /**
     * Return the arrival airport
     * @return
     */
    public String getAirportArr() {
        return airportArr;
    }

    /**
     * Return the depart zoned date and time
     * @return
     */
    public ZonedDateTime getTimeDep() {
        return timeDep;
    }

    /**
     * Return the arrival zoned date and time
     * @return
     */
    public ZonedDateTime getTimeArr() {
        return timeArr;
    }


    /**
     *
     * @return a string representation of the flight
     */
    @Override
    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", airline='" + airline + '\'' +
                ", airportDep='" + airportDep + '\'' +
                ", airportArr='" + airportArr + '\'' +
                ", timeDep=" + timeDep +
                ", timeArr=" + timeArr +
                '}';
    }

    /**
     * JSON representation of the flight
     * @param out the stream to which the JSON representation of the {@code Resource} has to be written.
     *
     * @throws IOException
     */
    @Override
    public void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("flight");
        jg.writeStartObject();
        jg.writeStringField("id", getId());
        jg.writeStringField("airline", getAirline());
        jg.writeStringField("airportDep", getAirportDep());
        jg.writeStringField("airportArr", getAirportArr());
        jg.writeStringField("timeDep", getTimeDep().toString());
        jg.writeStringField("timeArr", getTimeArr().toString());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a flight object from JSON representation
     * @param in
     * @return
     * @throws IOException
     */
    public static Flight fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jAirline= null;
        String jAirportDep= null;
        String jAirportArr= null;
        ZonedDateTime jTimeDep= null;
        ZonedDateTime jTimeArr= null;
        String jId = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "airport".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Airport object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "id":
                        jp.nextToken();
                        jId = jp.getText();
                        break;
                    case "airline":
                        jp.nextToken();
                        jAirline = jp.getText();
                        break;
                    case "airportDep":
                        jp.nextToken();
                        jAirportDep = jp.getText();
                        break;
                    case "airportArr":
                        jp.nextToken();
                        jAirportArr = jp.getText();
                        break;
                    case "timeDep":
                        jp.nextToken();
                        jTimeDep = ZonedDateTime.parse(jp.getText());
                        break;
                    case "timeArr":
                        jp.nextToken();
                        jTimeArr = ZonedDateTime.parse(jp.getText());
                        break;
                }
            }
        }

        return new Flight(jId, jAirline, jAirportDep, jAirportArr, jTimeDep,jTimeArr);
    }
}
