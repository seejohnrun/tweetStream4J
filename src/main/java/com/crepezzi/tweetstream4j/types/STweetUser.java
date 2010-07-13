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

import com.crepezzi.tweetstream4j.ext.OptionalGson;
import com.google.gson.JsonObject;

/**
 * A class representing a user in the twitterverse.
 * @author jcrepezzi
 */
public final class STweetUser {

    private Boolean profileBackgroundTile, verified, geoEnabled, notifications, isProtected, following;
    private String url, profileSidebarBorderColor, description, profileBackgroundColor,
            profileTextColor, profileImageUrl, timeZone, location, name, profileLinkColor, screenName, profileBackgroundImageUrl;
    private Integer followersCount, friendsCount, favouritesCount, statusesCount, utcOffset;
    private String createdAt;
    private Long userId;
    private transient JsonObject json;

    private STweetUser() {
        //no creating tweet users
    }

    /**
     * Parse a user from incoming JSON (from twitter streaming API)
     * @param obj The JSON object to parse
     * @return The resultant STweetUser
     */
    static STweetUser parseJSON(JsonObject obj) {
        STweetUser user = new STweetUser();
        user.json = obj;
        user.userId = obj.get("id").getAsLong();
        user.screenName = obj.get("screen_name").getAsString();
        user.profileBackgroundTile = obj.get("profile_background_tile").getAsBoolean();
        user.verified = obj.get("verified").getAsBoolean();
        user.geoEnabled = obj.get("geo_enabled").getAsBoolean();
        user.notifications = OptionalGson.getAsBoolean(obj, "notifications");
        user.following = OptionalGson.getAsBoolean(obj, "following");
        user.isProtected = obj.get("protected").getAsBoolean();
        user.url = OptionalGson.getAsString(obj, "url");
        user.profileSidebarBorderColor = obj.get("profile_sidebar_border_color").getAsString();
        user.description = OptionalGson.getAsString(obj, "description");
        user.profileBackgroundColor = obj.get("profile_background_color").getAsString();
        user.profileTextColor = obj.get("profile_text_color").getAsString();
        user.profileImageUrl = obj.get("profile_image_url").getAsString();
        user.timeZone = OptionalGson.getAsString(obj, "time_zone");
        user.location = OptionalGson.getAsString(obj, "location");
        user.name = obj.get("name").getAsString();
        user.profileLinkColor = obj.get("profile_link_color").getAsString();
        user.profileBackgroundImageUrl = obj.get("profile_background_image_url").getAsString();
        user.followersCount = obj.get("followers_count").getAsInt();
        user.friendsCount = obj.get("friends_count").getAsInt();
        user.favouritesCount = obj.get("favourites_count").getAsInt();
        user.statusesCount = obj.get("statuses_count").getAsInt();
        user.createdAt = obj.get("created_at").getAsString();
        user.utcOffset = OptionalGson.getAsInt(obj, "utc_offset");
        return user;
    }

    public JsonObject getJSON() {
        return this.json;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public Boolean getFollowing() {
        return following;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public Boolean getGeoEnabled() {
        return geoEnabled;
    }

    public Boolean getIsProtected() {
        return isProtected;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Boolean getNotification() {
        return notifications;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public Boolean getProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getUrl() {
        return url;
    }

    public Long getUserId() {
        return userId;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public Boolean getVerified() {
        return verified;
    }

}
