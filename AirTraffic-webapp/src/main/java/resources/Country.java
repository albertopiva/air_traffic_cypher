/**
 * Resource that represents a Country
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


public class Country extends Resource{

    final private String isoCode;
    final private String name;

    /**
     * Constructor with paramters
     * @param isoCode the isoCode of the country
     * @param name the name of the country
     */
    public Country(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    /**
     * @return the name of the country
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the isocode of the country
     */
    public String getIsoCode() {
        return isoCode;
    }


    /**
     *
     * @return a string representation of the country
     */
    @Override
    public String toString(){
        return "IsoCode: "+isoCode+", name: "+name;
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
        jg.writeFieldName("country");
        jg.writeStartObject();
        jg.writeStringField("isoCode", getIsoCode());
        jg.writeStringField("name", getName());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Country} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Country} object.
     * @return the {@code Country} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Country fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jName = null;
        String jCode= null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "country".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Country object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "isoCode":
                        jp.nextToken();
                        jCode = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                }
            }
        }

        return new Country(jCode, jName);
    }
}
