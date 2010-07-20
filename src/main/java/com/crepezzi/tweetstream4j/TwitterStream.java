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

import com.crepezzi.tweetstream4j.types.SDeletion;
import com.crepezzi.tweetstream4j.types.SLimit;
import com.crepezzi.tweetstream4j.types.STweet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Object used to connect to a stream URL and read from it, passing any
 * valid responses to the handler specified in the constructor.
 * @author jcrepezzi
 */
public class TwitterStream implements Runnable {

    private URL url;
    private URLConnection conn;
    private BufferedReader br;

    private TwitterStreamHandler handler;

    private static Log logger = LogFactory.getLog(TwitterStream.class);

    private boolean stopRequested = false;

    private String postContents;
    private static final int DEFAULT_TIMEOUT = 1000, STEP_TIMEOUT = 1000, MAX_TIMEOUT = 16000;
    private int timeout = DEFAULT_TIMEOUT;

    /**
     * Start a stream with a given connection
     * @param urlString The url to connect to
     * @param postContents The post contents as a string (null on none)
     * @param handler The handler to send items to
     * @param authb64 A base64 encoding of the Credentials
     */
    TwitterStream(String urlString, String postContents, TwitterStreamHandler handler) {
        this.postContents = postContents;
        try {
            this.url = new URL(urlString);
            logger.debug("urlString: '" + urlString + "' / postContents: '" + postContents + "'");
        } catch (MalformedURLException ex) {
            logger.error("Logger is potentially flawed or outdated. " + ex.getLocalizedMessage());
        }
    }

    TwitterStream(HttpURLConnection conn, String postBody, TwitterStreamHandler handler) {
        this.postContents = postBody;
        this.conn = conn;
        this.handler = handler;
        logger.debug("urlString: '" + conn.getURL().toString() + "' / postContents: '" + postContents + "'");
    }

    /**
     * Set the handler (used if wanting to modify handler mid-stream)
     * @param handler The handler to substitute
     */
    public void setHandler(TwitterStreamHandler handler) {
        this.handler = handler;
    }

    /**
     * Stop waiting for new tweets.  Signals the close of loop structures
     * so outside threads may close
     */
    public void stop() {
        this.stopRequested = true;
    }

    /**
     * Continue to open connections, linearly falling backwards on failure.
     */
    @Override
    public void run() {
        while (true) {
            try {
                //open a connection
                this.doConnection();
                //continuously read tweets and other things, passing them to the handler as appropriate
                //@TODO, make this non-blocking
                this.readIncoming();
                //successful, reset timeout
                this.timeout = DEFAULT_TIMEOUT;
            } catch (IOException ex) {
                logger.error("IO Exception! Trying again in " + timeout + "ms! " + ex.getMessage());
                //wait a certain amount of time
                sleepMilli(this.timeout);
                //up the timeout to the next appropriate value
                timeout += STEP_TIMEOUT;
                if (timeout > MAX_TIMEOUT) timeout = MAX_TIMEOUT;
            }
            if (this.stopRequested) break;
        }
        handler.stop(); // Signal to the handler
    }

    /**
     * Open a connection and send post contents across, open a new reader to
     * accept incoming data (stream).
     * @throws IOException Connection problem
     */
    private void doConnection() throws IOException {
        //set up an output stream
        if (this.postContents != null) {
            OutputStream os = this.conn.getOutputStream();
            os.write(this.postContents.getBytes());
        }
        //set up an input stream, UTF-8
        br = new BufferedReader(new InputStreamReader(this.conn.getInputStream(), "UTF-8"));
    }

    /**
     * Sleep for a certain number of milliseconds
     * @param timeout Number of milliseconds to sleep
     */
    private void sleepMilli(int timeout) {
        try { Thread.sleep(timeout); } catch (InterruptedException ex) { }
    }

    /**
     * Read an incoming line and attempt to parse it.  Continue this until
     * asked to stop or a connection issue is encountered
     * @throws IOException A connection issue was encountered
     */
    private void readIncoming() throws IOException {
        String line;
        while((line = br.readLine()) != null) {
            try {
                if (line.isEmpty()) continue; //skip empty lines
                parseIncoming(line);
            } catch (JsonParseException ex) {
                logger.error("API possibly broken, Twitter possibly broken! " + ex.getMessage());
            }
            if (this.stopRequested) break;
        }
    }

    /**
     * Parse incoming lines, determine what type they are and act accordingly.
     * @param line The line to parse (JSON)
     */
    private void parseIncoming(String line) {
        if (line == null) return;
        //parse with JSON
        JsonParser parser = new JsonParser();
        JsonObject elem = parser.parse(line).getAsJsonObject();
        if (elem.has("delete")) parseDeletion(elem);
        else if (elem.has("limit")) parseLimit(elem);
        else if (elem.has("text")) parseTweet(elem);
        //otherwise, we don't have a proper type to work with, escape gracefully
        else logger.error("Got a bad line! API may be out of date! " + line);
    }

    /**
     * Parse deletion objects and give them to the handler
     * @param obj The object to parse
     */
    private void parseDeletion(JsonObject elem) {
        SDeletion del = SDeletion.parseJSON(elem);
        if (del != null) this.handler.addDeletion(del);
    }

    /**
     * Parse limit objects and give them to the handler
     * @param obj The object to parse
     */
    private void parseLimit(JsonObject elem) {
        SLimit lim = SLimit.parseJSON(elem);
        if (lim != null) this.handler.addLimit(lim);
    }

    /**
     * Parse tweet objects and give them to the handler
     * @param obj The object to parse
     */
    private void parseTweet(JsonObject elem) {
        STweet tweet = STweet.parseJSON(elem);
        if (tweet != null) this.handler.addTweet(tweet);
    }

}
