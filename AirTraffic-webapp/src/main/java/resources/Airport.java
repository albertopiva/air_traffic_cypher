/**
 * Resource that represents an Airport
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


public class Airport extends Resource{

    final private String icao;
    final private String cityName;
    final private String name;

    /**
     * Constructor with parameters
     * @param icao the icao code of the airport
     * @param name the name of the airport
     * @param city the city of the airport
     */
    public Airport(String icao, String name, String city) {
        this.icao = icao;
        this.name = name;
        this.cityName = city;
    }

    /**
     *
     * @return the name of the airport
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the city of the airport
     */
    public String getCityName() {
        return cityName;
    }

    /**
     *
     * @return the icao code of the airport
     */
    public String getIcao() {
        return icao;
    }


    /**
     *
     * @return a String representation of the airport
     */
    @Override
    public String toString(){
        return "Name: "+ name +", icao: "+icao+", city: "+cityName;
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
        jg.writeFieldName("airport");
        jg.writeStartObject();
        jg.writeStringField("icao", getIcao());
        jg.writeStringField("name", getName());
        jg.writeStringField("city", getCityName());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Airport} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Airport} object.
     * @return
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Airport fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jName = null;
        String jCity= null;
        String jIcao = null;

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
                    case "icao":
                        jp.nextToken();
                        jIcao = jp.getText();
                        break;
                    case "city":
                        jp.nextToken();
                        jCity = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                }
            }
        }

        return new Airport(jIcao, jName, jCity);
    }
}
