package com.crepezzi.tweetStream4j;

import com.crepezzi.tweetstream4j.types.STweet;
import com.google.gson.JsonObject;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author sscanlon
 */
public class STweetTest {

    private JsonObject json = makeMockJsonObj();
    private STweet obj = STweet.parseJSON(json);

    @Test
    public void testEquals() throws Exception {
        assertFalse(obj == null);
        assertFalse(obj.equals(new Object()));
        assertTrue(obj.equals(obj));

        STweet obj1 = STweet.parseJSON(json);
        STweet obj2 = STweet.parseJSON(json);
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testHashCode() {
        assertNotNull(obj.hashCode());
    }

    @Test
    public void testToString() {
    	assertNotNull(obj.toString());
    }

    /*
     * JSONObject is very cranky about trying to access non-existent fields so
     * we have to provide all of the non "optional" fields
     */
    private JsonObject makeMockJsonObj() {
        String[] strings = new String[] { "screen_name", "text", "url",
            "in_reply_to_screen_name", "source", "created_at",
            "profile_sidebar_border_color", "description",
            "profile_link_color", "profile_background_color",
            "profile_text_color", "profile_image_url", "time_zone",
            "profile_background_image_url",
            "location", "name" };
        String[] booleans = new String[] {
            "favorited", "truncated", "profile_background_tile", "verified", "geo_enabled",
            "protected"
        };
        String[] ints = new String[] {
            "followers_count", "friends_count", "favourites_count", "statuses_count"
        };

        JsonObject json_object = new JsonObject();
        for (String field : strings) {
            json_object.addProperty(field, "");
        }
        for (String field : booleans) {
            json_object.addProperty(field, false);
        }
        for (String field : ints) {
            json_object.addProperty(field, 0);
        }
        
        json_object.addProperty("id", 1234L);
        json_object.add("user", json_object);
        return json_object;
    }

}
