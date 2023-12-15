/**
 * Resource that represents an Airline Company
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


public class Airline extends Resource{

    final private String icao;
    final private String name;

    /**
     * Constructor with parameters
     * @param icao icao code of the airline company
     * @param name name of the airline company
     */
    public Airline(String icao, String name) {
        this.icao = icao;
        this.name = name;
    }

    /**
     *
     * @return the icao code of the airline company
     */
    public String getIcao() {
        return icao;
    }
    /**
     * @return the name of the airline company
     */
    public String getName() {
        return name;
    }


    /**
     *
     * @return a String representation of the airline company
     */
    @Override
    public String toString(){
        return "Name: "+ name +", icao: "+icao;
    }

    /**
     * JSON representation of the airline company
     * @param out the stream to which the JSON representation of the {@code Resource} has to be written.
     *
     * @throws IOException
     */
    @Override
    public void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("airline");
        jg.writeStartObject();
        jg.writeStringField("icao", getIcao());
        jg.writeStringField("name", getName());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Airline} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Airline} object.
     * @return
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Airline fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jName = null;
        String jCity= null;
        String jIcao = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "airline".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Airline object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "icao":
                        jp.nextToken();
                        jIcao = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                }
            }
        }

        return new Airline(jIcao, jName);
    }
}
