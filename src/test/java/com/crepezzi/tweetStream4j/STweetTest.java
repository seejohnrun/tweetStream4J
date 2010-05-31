package com.crepezzi.tweetStream4j;

import com.crepezzi.tweetstream4j.types.STweet;
import static org.junit.Assert.*;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * @author sscanlon
 */
public class STweetTest {

    private JSONObject json = makeMockJsonObj();
    private STweet obj = STweet.parseJSON(json);

    @Test
    public void testEquals() throws Exception {
        assertFalse(obj.equals(null));
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
    private JSONObject makeMockJsonObj() {
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
        
        JSONObject json = new JSONObject();
        for (String field : strings) {
            json.accumulate(field, "");
        }
        for (String field : booleans) {
            json.accumulate(field, false);
        }
        for (String field : ints) {
            json.accumulate(field, 0);
        }
        json.accumulate("id", 1234L);
        json.accumulate("user", json);
        return json;
    }

}
