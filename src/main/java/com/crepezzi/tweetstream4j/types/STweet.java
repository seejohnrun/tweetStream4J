/*
Copyright (c) 2010, John Crepezzi <john@crepezzi.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.crepezzi.tweetstream4j.types;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import net.sf.json.JSONObject;

/**
 * A Class representing a streamed tweet from the Twitter streaming API.
 * 
 * @author jcrepezzi
 */
public final class STweet {

    private Boolean favorited, truncated;
    private Long inReplyToUserId, inReplyToStatusId, statusId;
    private String text, inReplyToScreenName, source, createdAt;
    private STweetUser user;
    private STweetGeo geo;
    private JSONObject json;

    protected STweet() {
	  // no creating tweets
    }

    /**
     * Parse the JSON of an incoming tweet and return an STweet instance
     * 
     * @param obj
     *            The JSON object to parse
     * @return The resultant STweet
     */
    public static STweet parseJSON(JSONObject obj) {
        STweet tweet = new STweet();
        tweet.json = obj;
        tweet.favorited = obj.getBoolean("favorited");
        tweet.truncated = obj.getBoolean("truncated");
        tweet.inReplyToUserId = obj.optLong("in_reply_to_user_id");
        tweet.inReplyToStatusId = obj.optLong("in_reply_to_status_id");
        tweet.statusId = obj.getLong("id");
        tweet.text = obj.getString("text");
        tweet.inReplyToScreenName = obj.getString("in_reply_to_screen_name");
        tweet.source = obj.getString("source");
        tweet.createdAt = obj.getString("created_at");
        tweet.user = STweetUser.parseJSON(obj.getJSONObject("user"));

        // get geo
        JSONObject t = obj.optJSONObject("geo");
        if (t != null && !t.isNullObject())
            tweet.geo = STweetGeo.parseJSON(t);

        return tweet;
    }

    public JSONObject getJSON() {
        return this.json;
    }

    @Override
    public String toString() {
    	return new ToStringBuilder(null)
                .append("statusId", statusId)
                .append("tweet", text)
                .append("user", user)
                .toString();
    }

    /**
     * Equality is based on statusId
     */
    @Override
    public boolean equals(Object obj) {
  	    if (obj == this) return true;
	    if (!(obj instanceof STweet)) return false;
	    STweet rhs = (STweet) obj;
	    return new EqualsBuilder().append(this.statusId, rhs.statusId).isEquals();
    }

    @Override
    public int hashCode() {
	    return new HashCodeBuilder(-1944286887, -1020224145).append(statusId).toHashCode();
    }

    public String getCreatedAt() {
	    return createdAt;
    }

    public Boolean getFavorited() {
	    return favorited;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public Long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public Long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public String getSource() {
        return source;
    }

    public Long getStatusId() {
        return statusId;
    }

    public String getText() {
        return text;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public STweetUser getUser() {
        return user;
    }

    public STweetGeo getGeo() {
        return geo;
    }

}
