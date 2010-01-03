/*
    John Crepezzi (c) 2009
    <john@crepezzi.com>
*/
import com.crepezzi.tweetriver.TweetRiver;
import com.crepezzi.tweetriver.TwitterStream;
import com.crepezzi.tweetriver.TwitterStreamConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author based
 */
public class APITest {

    private static final TwitterStreamConfiguration tws = new TwitterStreamConfiguration("seejohnrun", "xxxxxx");

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

    private static void waitSomeTime(int i) {
        long t1;
        long t0 = t1 = System.currentTimeMillis();
        while (t0 + i > t1) t1 = System.currentTimeMillis();
    }
    
}