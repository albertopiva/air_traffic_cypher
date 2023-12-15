/**
 * Resource that represents an Airspace
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


public class Airspace extends Resource{

    final private String icao;
    final private String type;
    final private String country;

    /**
     * Constructor with paramters
     * @param icao the icao code of the airspace
     * @param type the type of the airspace (should be FIR or UIR)
     * @param country the name of the airspace
     */
    public Airspace( String icao, String type, String country) {
        this.icao = icao;
        this.type = type;
        this.country = country;
    }

    /**
     *
     * @return the name of the airspace
     */
    public String getCountry() {
        return country;
    }


    /**
     *
     * @return the type of the airspace
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return the icao code of the airspace
     */
    public String getIcao() {
        return icao;
    }

    /**
     *
     * @return a string representation of the airspace
     */

    @Override
    public String toString(){
        return "icao: "+ icao +", type: "+type+", name: "+ country;
    }

    /**
     * JSON representation of the object
     * @param out the stream to which the JSON representation of the {@code Resource} has to be written.
     *
     * @throws IOException
     */
    @Override
    public void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("airspace");
        jg.writeStartObject();
        jg.writeStringField("icao", getIcao());
        jg.writeStringField("type", getType());
        jg.writeStringField("country", getCountry());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Airspace} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Airspace} object.
     * @return the {@code Airspace} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Airspace fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jName = null;
        String jCode= null;
        String jType= null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "airspace".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Airspace object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "icao":
                        jp.nextToken();
                        jCode = jp.getText();
                        break;
                    case "type":
                        jp.nextToken();
                        jType = jp.getText();
                        break;
                    case "country":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                }
            }
        }

        return new Airspace(jCode, jType, jName);
    }
}
