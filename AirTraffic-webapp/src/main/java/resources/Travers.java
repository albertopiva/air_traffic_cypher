/**
 * Resource that represents the Travers Relation with its Airspace
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */package resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;


public class Travers extends Airspace {

    final private ZonedDateTime entryTime;
    final private ZonedDateTime exitTime;
    final private int sequence;
    final private String id;

    /**
     * Constructor with parameters
     * @param id the id of the flight
     * @param icao the icao code of the airspace
     * @param type the type of the airspace (should be FIR or UIR)
     * @param name the name of the airspace
     * @param sequence the number of the airspace traversed by the flight
     * @param entryTime the time in which the flight enter in this airspace
     * @param exitTime the time in which the flight exit from this airspace
     */
    public Travers( String icao, String type, String name, String id, int sequence, ZonedDateTime entryTime, ZonedDateTime exitTime) {
        super(icao,type,name);
        this.id = id;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.sequence = sequence;
    }

    /**
     *
     * @return the id of the flight
     */
    public String getId() {
        return id;
    }


    /**
     *
     * @return the zoned entry time
     */
    public ZonedDateTime getEntryTime() {
        return entryTime;
    }
    /**
     *
     * @return the zoned exit time
     */
    public ZonedDateTime getExitTime() {
        return exitTime;
    }
    /**
     *
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }


    /**
     *
     * @return a string representation of the travers
     */
    @Override
    public String toString(){
        return super.toString()+ "sequence: "+ this.sequence +", entryTime: "+entryTime+", exitTime: "+exitTime;
    }

    /**
     * JSON representation
     * @param out the stream to which the JSON representation of the {@code Resource} has to be written.
     *
     * @throws IOException
     */
    @Override
    public void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("travers");
        jg.writeStartObject();
        jg.writeStringField("icao", super.getIcao());
        jg.writeStringField("type", super.getType());
        jg.writeStringField("country", super.getCountry());
        jg.writeStringField("id", getId());
        jg.writeStringField("entryTime", getEntryTime().toString());
        jg.writeStringField("exitTime", getExitTime().toString());
        jg.writeNumberField("sequence",getSequence());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Travers} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Travers} object.
     * @return the {@code Travers} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Travers fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jName = null;
        String jId = null;
        String jCode= null;
        String jType= null;
        ZonedDateTime jEntry= null;
        ZonedDateTime jExit= null;
        int jSequence = -1;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "travers".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Travers object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "icao":
                        jp.nextToken();
                        jCode = jp.getText();
                        break;
                    case "id":
                        jp.nextToken();
                        jId = jp.getText();
                        break;
                    case "type":
                        jp.nextToken();
                        jType = jp.getText();
                        break;
                    case "country":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                    case "entryTime":
                        jp.nextToken();
                        jEntry= ZonedDateTime.parse(jp.getText());
                        break;
                    case "exitTime":
                        jp.nextToken();
                        jExit = ZonedDateTime.parse(jp.getText());
                        break;
                    case "sequence":
                        jp.nextToken();
                        jSequence = Integer.parseInt(jp.getText());
                        break;
                }
            }
        }

        return new Travers(jCode, jType, jName, jId, jSequence,jEntry, jExit);
    }
}
