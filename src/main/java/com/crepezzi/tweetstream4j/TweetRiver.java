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

package com.crepezzi.tweetstream4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 * Class for creating instances of TwitterStream
 * @author jcrepezzi
 */
public class TweetRiver {

    private static final Log logger = LogFactory.getLog(TweetRiver.class);
    private static final String API_URL = "http://stream.twitter.com/1/";

    private static final String ENCODING = "UTF-8";

    private static final String
            SAMPLE_URL = API_URL + "statuses/sample.json",
            FILTER_URL = API_URL + "statuses/filter.json",
            RETWEET_URL = API_URL + "statuses/retweet.json",
            LINKS_URL = API_URL + "statuses/links.json",
            FIREHOSE_URL = API_URL + "statuses/firehose.json";

    /**
     * Do not create instances of tweetriver
     */
    private TweetRiver() { }

    /**
     * Method to access the Twitter Streaming API's 'filter' method.
     * from twitter:
     *      Returns public statuses that match one or more filter predicates.
     *      At least one predicate parameter, track or follow, must be specified.
     *      Both parameters may be specified which allows most clients to use a
     *      single connection to the Streaming API. Placing long parameters in
     *      the URL may cause the request to be rejected for excessive URL
     *      length. Use a POST request header parameter to avoid long URLs.
     *
     *      The default access level allows up to 200 track keywords and 400
     *      follow userids. Increased access levels allow 80,000 follow userids
     *      ("shadow" role), 400,000 follow userids ("birddog" role), 10,000
     *      track keywords ("restricted track" role), and 200,000 track keywords
     *      ("partner track" role). Increased track access levels also pass a
     *      higher proportion of statuses before limiting the stream.
     * @param tws A TwitterStreamConfiguration object which contains the username
     *            and password to use as well as any count / delimited parameters
     * @param handler The handler to send results to
     * @param follows The list of user ids to filter through
     * @param tracks The list of tracks to filter through
     * @param locations The list of location bounding boxes to track
     * @return A TwitterStream (extends Runnable) which will stream tweets following
     *         the filter parameters.
     */
    public static TwitterStream filter(TwitterStreamConfiguration tws, TwitterStreamHandler handler,
            Collection<Long> follows, Collection<String> tracks, Collection<String> locations) throws IOException, OAuthException {

        String postBody = buildFilterContents(follows, tracks, locations);
        //build get params
        HttpParams getParams = new BasicHttpParams();
        if (tws.getCount() != null) getParams.setIntParameter("count", tws.getCount());
        if (tws.isDelimited()) getParams.setParameter("delimited", tws.getDelimited());
        //send request
        HttpRequestBase conn = buildConnection(FILTER_URL, getParams, tws, postBody);
        return new TwitterStream(conn, handler);
    }

    /**
     * API Method to access the 'retweet' method of the Twitter Streaming API.
     * from twitter:
     *      Returns all retweets. The retweet stream is not a generally
     *      available resource. Few applications require this level of access.
     *      Creative use of a combination of other resources and various access
     *      levels can satisfy nearly every application use case.
     * @param tws A TwitterStreamConfiguration object which contains the username
     *            and password to use as well as any count / delimited parameters
     * @param handler The handler to send results to
     * @return A TwitterStream (extends Runnable) which will stream tweets
     *         returned from this api call.
     */
    public static TwitterStream retweet(TwitterStreamConfiguration tws, TwitterStreamHandler handler)
            throws IOException, OAuthException {
        
        //build get params
        HttpParams getParams = new BasicHttpParams();
        if (tws.isDelimited()) getParams.setParameter("delimited", tws.getDelimited());
        //send request
        HttpRequestBase conn = buildConnection(RETWEET_URL, getParams, tws);
        return new TwitterStream(conn, handler);
    }

    /**
     * API Method to access the 'retweet' method of the Twitter Streaming API.
     * from twitter:
     *      Returns all retweets. The retweet stream is not a generally
     *      available resource. Few applications require this level of access.
     *      Creative use of a combination of other resources and various access
     *      levels can satisfy nearly every application use case.
     * @param tws A TwitterStreamConfiguration object which contains the username
     *            and password to use as well as any count / delimited parameters
     * @param handler The handler to send results to
     * @return A TwitterStream (extends Runnable) which will stream tweets
     *         returned from this api call.
     */
    public static TwitterStream links(TwitterStreamConfiguration tws, TwitterStreamHandler handler)
            throws IOException, OAuthException {

        //build get params
        HttpParams getParams = new BasicHttpParams();
        if (tws.getCount() != null) getParams.setIntParameter("count", tws.getCount());
        if (tws.isDelimited()) getParams.setParameter("delimited", tws.getDelimited());
        //send request
        HttpRequestBase conn = buildConnection(LINKS_URL, getParams, tws);
        return new TwitterStream(conn, handler);
    }

    /**
     * API Method to access the 'firehose' method of the Twitter Streaming API.
     * from twitter:
     *      Returns all public statuses. The Firehose is not a generally
     *      available resource. Few applications require this level of access.
     *      Creative use of a combination of other resources and various access
     *      levels can satisfy nearly every application use case.
     * @param tws A TwitterStreamConfiguration object which contains the username
     *            and password to use as well as any count / delimited parameters
     * @param handler The handler to send results to
     * @return A TwitterStream (extends Runnable) which will stream tweets
     *         returned from this api call.
     */
    public static TwitterStream firehose(TwitterStreamConfiguration tws, 
            TwitterStreamHandler handler) throws IOException, OAuthException {
        
        //build get params
        HttpParams getParams = new BasicHttpParams();
        if (tws.getCount() != null) getParams.setIntParameter("count", tws.getCount());
        if (tws.isDelimited()) getParams.setParameter("delimited", tws.getDelimited());
        //send request
        HttpRequestBase conn = buildConnection(FIREHOSE_URL, getParams, tws);
        return new TwitterStream(conn, handler);
    }

    /**
     * API Method to access the 'sample' method of the Twitter Streaming API.
     * from twitter:
     *      Returns a random sample of all public statuses. The default access
     *      level provides a small proportion of the Firehose. The "Gardenhose"
     *      access level provides a proportion more suitable for data mining and
     *      research applications that desire a larger proportion to be
     *      statistically significant sample.
     * @param tws A TwitterStreamConfiguration object which contains the username
     *            and password to use as well as any count / delimited parameters
     * @param handler The handler to send results to
     * @return A TwitterStream (extends Runnable) which will stream tweets
     *         returned from this api call.
     */
    public static TwitterStream sample(TwitterStreamConfiguration tws,
            TwitterStreamHandler handler) throws IOException, OAuthException {

        //build get params
        HttpParams getParams = new BasicHttpParams();
        if (tws.getCount() != null) getParams.setIntParameter("count", tws.getCount());
        if (tws.isDelimited()) getParams.setParameter("delimited", tws.getDelimited());
        //send request
        HttpRequestBase request = buildConnection(SAMPLE_URL, getParams, tws);
        return new TwitterStream(request, handler);
    }

    // Convenience Method
    private static HttpRequestBase buildConnection(String urlString, HttpParams params, TwitterStreamConfiguration tws) throws OAuthException {
        return buildConnection(urlString, params, tws, null);
    }

    // Build a connection
    private static HttpRequestBase buildConnection(String base, HttpParams params,
            TwitterStreamConfiguration tws, String postContent) throws OAuthException {

        if (postContent != null) {
            HttpPost method = new HttpPost(base);
            method.setParams(params);
            try {
                // Set the content to be the entity
                StringEntity postEntity = new StringEntity(postContent, "UTF-8");
                postEntity.setContentType("application/x-www-form-urlencoded");
                method.setEntity(postEntity);
            } catch (UnsupportedEncodingException ex) {
                logger.fatal("Unsupported encoding", ex);
            }
            tws.getConsumer().sign(method);
            return method;
        }

        else {
            HttpGet method = new HttpGet(base);
            method.setParams(params);
            tws.getConsumer().sign(method);
            return method;
        }

    }

    /**
     * Given a base URL and a parameter list, build a valid URL, of the form:
     * API_URL + base + ? + param1=value1 + & + param2=value2 ...
     * @param base Base URL String
     * @param params List of parameters
     * @return A URL String fitting the specified conditions
     */
    private static String buildUrlString(String base, Map<String, String> params) {
        if (params == null || params.isEmpty()) return API_URL + base;
        //construct get parameters list
        StringBuilder get = new StringBuilder();
        boolean first = true;
        for (String param : params.keySet()) {
            if (!first) get.append('&'); else first = false;
            //add the parameter
            get.append(param);
            String value = params.get(param);
            if (value != null) {
                get.append('=');
                get.append(value);
            }
        }
        //return the full string
        if (get.length() == 0) return API_URL + base;
        return API_URL + base + "?" + get.toString();
    }

    /**
     * Build a post body for a filter request given a collection of users
     * to follow (Long), and a collection of keywords to track (String).  Of
     * the form:
     *      follow=1,2,3&track=one,two,three
     * @param follows A collection of Users to follow :: Collection<Long>
     * @param tracks A collection of Keywords to track :: Collection<String>
     * @param locations A collection of Bounding boxes to track :: Collection<String>
     * @return A string representing the given parameters in the proper form.
     *         Note: tracks will be URL encoded for HTTP.
     * @throws IllegalArgumentException if any of the tracks contain
     *         spaces.  Reference: http://apiwiki.twitter.com/Streaming-API-Documentation#track
     */
    private static String buildFilterContents(Collection<Long> follows, Collection<String> tracks, Collection<String> locations) throws UnsupportedEncodingException {
        List<String> pieces = new ArrayList<String>();
        if (follows != null && follows.size() > 0) pieces.add(list(follows, "follow=", ","));

        if (tracks != null && tracks.size() > 0) {
            Collection<String> encodedTracks = new ArrayList<String>(tracks.size());
            for (String track : tracks) encodedTracks.add(URLEncoder.encode(track, ENCODING));
            pieces.add(list(encodedTracks, "track=", ","));
        }

        if (locations != null && locations.size() > 0) pieces.add(list(locations, "locations=", ","));
        return list(pieces, "", "&");
    }

    // Used to build URL segments
    private static String list(Iterable iterable, String initialString, String delimiter) {
        Iterator iterator = iterable.iterator();
        if (!iterator.hasNext()) return ""; // empty?
        StringBuilder buffer = new StringBuilder(initialString + iterator.next().toString());
        while (iterator.hasNext()) buffer.append(delimiter).append(iterator.next().toString());
        return buffer.toString();
    }

}
