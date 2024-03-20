package org.swiggy.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * <p>
 * Handles the methods to work with json array.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class JsonArray {

    private static final JacksonFactory JACKSON_WRAPPER = JacksonFactory.getInstance();
    private ArrayNode arrayNode;

    public JsonArray(final JsonNode jsonNode) {
        this.arrayNode = (ArrayNode) jsonNode;
    }

    /**
     * <p>
     * Creates the json array object by wrapping the json node object.
     * </p>
     *
     * @param jsonNode the json node object
     * @return json array object
     */
    private JsonArray create(final JsonNode jsonNode) {
        return new JsonArray(jsonNode);
    }

    /**
     * <p>
     * Builds the json array with the given object.
     * </p>
     *
     * @param object the object to be converted into json array
     * @return JsonArray object
     */
    public JsonArray build(final Object object) {
        return create(JACKSON_WRAPPER.toJson(object));
    }

    /**
     * <p>
     * converts the array node into byte array.
     * </p>
     *
     * @return The byte array of array node
     */
    public byte[] asBytes() {
        return JACKSON_WRAPPER.asByteArray(arrayNode);
    }

    /**
     * <p>
     * Adds the json object to the array node.
     * </p>
     *
     * @param jsonObject The json object
     */
    public void add(final JsonObject jsonObject) {
        arrayNode.add(jsonObject.getObjectNode());
    }

    /**
     * <p>
     *  checks the array node is empty or not
     * </p>
     *
     * @return true if the array node is empty, false otherwise
     */
    public boolean isEmpty() {
        return arrayNode.isEmpty();
    }
}
