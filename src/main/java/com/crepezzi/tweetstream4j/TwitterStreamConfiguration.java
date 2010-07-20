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

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A configuration class for describing to TweetRiver how to set up a TwitterStream.
 * @author jcrepezzi
 */
public final class TwitterStreamConfiguration {

    private String consumerKey, consumerSecret;
    private Integer count = null, delimitedLength = null;

    private static final String TWITTER_REQUEST = "https://api.twitter.com/oauth/request_token";
    private static final String TWITTER_ACCESS = "https://api.twitter.com/oauth/access_token";
    private static final String TWITTER_AUTHORIZE = "https://api.twitter.com/oauth/authorize";

    private static Log logger = LogFactory.getLog(TwitterStreamConfiguration.class);

    private String authUrl = null;
    private OAuthProvider provider;
    private OAuthConsumer consumer;

    public TwitterStreamConfiguration(String key, String secret) throws OAuthException, OAuthCommunicationException {
        this.setConsumerKeyAndSecret(key, secret);
    }
    
    /**
     * Get the authorization URL to visit.
     * @return
     */
    public String getAuthorizationUrl() throws OAuthException {
        provider = new DefaultOAuthProvider(TWITTER_REQUEST, TWITTER_ACCESS, TWITTER_AUTHORIZE);
        authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
        return this.authUrl;
    }

    /**
     * Authorize the app by passing the PIN retrieved by visiting
     * the URL returned by .getAuthorizationUrl
     * @param PIN The PIN to authorize with
     */
    public void authorize(String PIN) throws OAuthException, OAuthCommunicationException {
        this.provider.retrieveAccessToken(consumer, PIN);
    }

    /**
     * Get the consumer's token
     * @return The token
     */
    public String getToken() {
        return this.consumer.getToken();
    }

    /**
     * Get the consumer's token secret
     * @return The token secret
     */
    public String getTokenSecret() {
        return this.consumer.getTokenSecret();
    }

    /**
     * Set the token and secret on the consumer directly
     * @param token The token
     * @param tokenSecret The token secret
     */
    public void setTokenAndSecret(String token, String tokenSecret) {
        this.consumer.setTokenWithSecret(token, tokenSecret);
    }

    /**
     * Return whether or not the provider has been authorized
     * @return
     */
    public boolean isAuthorized() {
        return this.provider.isOAuth10a();
    }

    /**
     * Set the consumer key
     * @param key The consumer key
     * @param secret The consumer secret
     * Note: This will trigger a restart of the OAuth workflow.
     */
    public void setConsumerKeyAndSecret(String key, String secret) throws OAuthException, OAuthCommunicationException {
        this.consumerKey = key;
        this.consumerSecret = secret;
        this.restartOAuthWorkflow();
    }

    /**
     * get the count associated with this configuration object.
     * @return The count
     * @see setCount
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Set the count for all requests issued with this configuration object.
     * from twitter:
     *      Indicates the number of previous statuses to consider for delivery
     *      before transitioning to live stream delivery. On unfiltered streams,
     *      all considered statuses are delivered, so the number requested is
     *      the number returned. On filtered streams, the number requested is
     *      the number of statuses that are applied to the filter predicate,
     *      and not the number of statuses returned.
     * @param count The count for all calls with this twitter configuration object
     * @see getCount
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Get the delimited length associated with this configuration object.
     * @return The delimited length
     * @see setDelimitedLength
     */
    public Integer getDelimitedLength() {
        return delimitedLength;
    }

    /**
     * Set the delimited length for all requests issued with this configuration
     * object.
     * from twitter:
     *      Indicates that statuses should be delimited in the stream. Statuses
     *      are represented by a length, in bytes, a newline, and the status
     *      text that is exactly length bytes. Note that "keep-alive" newlines
     *      may be inserted before each length.
     * @param delimited_length The delimited length to set
     */
    public void setDelimitedLength(Integer delimited_length) {
        this.delimitedLength = delimited_length;
    }

    /**
     * Restart the OAuth workflow - used in case you have to change a
     * key or secret for any reason.
     * @throws OAuthException
     */
    private void restartOAuthWorkflow() throws OAuthException, OAuthCommunicationException {
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
    }

    protected OAuthConsumer getConsumer() {
        return this.consumer;
    }

}
