/*
    John Crepezzi (c) 2009
    <john@crepezzi.com>
*/
package com.crepezzi.tweetstream4j.ext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Convenience Class
 * @author based
 */
public class OptionalGson {

    private static JsonElement getElement(JsonObject obj, String key) {
        if (!obj.has(key)) return null;
        JsonElement elem = obj.get(key);
        if (elem.isJsonNull()) return null;
        return elem;
    }

    public static String getAsString(JsonObject obj, String key) {
        JsonElement elem = getElement(obj, key);
        return (elem == null) ? null : elem.getAsString();
    }

    public static Long getAsLong(JsonObject obj, String key) {
        JsonElement elem = getElement(obj, key);
        return (elem == null) ? null : elem.getAsLong();
    }

    public static Integer getAsInt(JsonObject obj, String key) {
        JsonElement elem = getElement(obj, key);
        return (elem == null) ? null : elem.getAsInt();
    }

    public static Boolean getAsBoolean(JsonObject obj, String key) {
        JsonElement elem = getElement(obj, key);
        return (elem == null) ? null : elem.getAsBoolean();
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String key) {
        JsonElement elem = getElement(obj, key);
        return (elem == null) ? null : elem.getAsJsonObject();
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String key) {
        JsonElement elem = getElement(obj, key);
        return (elem == null) ? null : elem.getAsJsonArray();
    }

}
