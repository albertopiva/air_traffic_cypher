/**
 * Resource that represents a generic Resource
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */
package resources;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Resource {
	/**
	 * The JSON factory to be used for creating JSON parsers and generators.
	 */
	protected static final JsonFactory JSON_FACTORY;

	static {
		// setup the JSON factory
		JSON_FACTORY = new JsonFactory();
		JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
	}

	/**
	 * Returns a JSON representation of the {@code Resource} into the given {@code OutputStream}.
	 *
	 * @param out the stream to which the JSON representation of the {@code Resource} has to be written.
	 *
	 * @throws IOException if something goes wrong during the parsing.
	 */
	public abstract void toJSON(final OutputStream out) throws IOException;
}
