/*
    John Crepezzi (c) 2009
    <john@crepezzi.com>
*/

import com.crepezzi.tweetstream4j.TweetRiver;
import com.crepezzi.tweetstream4j.TwitterStream;
import com.crepezzi.tweetstream4j.TwitterStreamConfiguration;
import com.crepezzi.tweetstream4j.TwitterStreamHandler;
import com.crepezzi.tweetstream4j.types.SDeletion;
import com.crepezzi.tweetstream4j.types.SLimit;
import com.crepezzi.tweetstream4j.types.STweet;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author based
 */
public class APITest {

    private static final TwitterStreamConfiguration tws = new TwitterStreamConfiguration("seejohnrun", "");

    @Test
    public void testSampleHappyPath() throws Exception {
        MyTwitterStreamHandler handler = new MyTwitterStreamHandler();

        TwitterStream ts = TweetRiver.sample(tws, handler);

        Thread t = (new Thread(ts));
        t.start();

        //stop after a certain number of seconds
        waitSomeTime(5000);
        ts.stop();

        //make sure that tweets came in
        assertTrue(handler.getTweetCount() > 0);
    }

    @Test
    public void testFilterHappyPath() throws Exception {
        MyTwitterStreamHandler handler = new MyTwitterStreamHandler();

        Collection<String> tracks = new ArrayList<String>();
        tracks.add("obama");
        TwitterStream ts = TweetRiver.filter(tws, handler, null, tracks);

        Thread t = (new Thread(ts));
        t.start();

        //stop after a certain number of seconds
        waitSomeTime(5000);
        ts.stop();

        //make sure some tweets came in
        assertTrue(handler.getTweetCount() > 0);
    }

    // Ensure that stop() gets called when stopping a stream
    @Test
    public void testCloseCalled() throws Exception {
        // Make a test class that keeps track of stop being called
        class TestTwitterStreamHandler implements TwitterStreamHandler {
            public boolean success = false;
            public void addTweet(STweet t) { }
            public void addLimit(SLimit l) { }
            public void addDeletion(SDeletion d) { }
            public void stop() {
                success = true;
            }
        }

        TestTwitterStreamHandler handler = new TestTwitterStreamHandler();
        TwitterStream ts = TweetRiver.sample(tws, handler);

        Thread t = (new Thread(ts));
        t.start();

        // Wait a bit for it to stop
        ts.stop();
        waitSomeTime(1000);

        // make sure stop was called
        assertTrue(handler.success);
         
    }

    private static void waitSomeTime(int i) {
        long t1;
        long t0 = t1 = System.currentTimeMillis();
        while (t0 + i > t1) t1 = System.currentTimeMillis();
    }
    
}