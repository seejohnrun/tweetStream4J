# TweetStream4J

*This is a Java binding for the Twitter API written in a way that will allow Java developers to quickly and successfully work with the Twitter API.*  
__Uses OAuth authentication__.

---

### Example 1:

    TwitterStreamConfiguration tws = new TwitterStreamConfiguration(KEY, SECRET);
    tws.getAuthorizationUrl(); // URL to visit for PIN
    tws.authorize(pin); // enter the pin from the site
    tws.getToken(); tws.getTokenSecret(); // hold onto these

    MyTwitterHandler handler = new MyTwitterHandler();    

    TwitterStream ts = TweetRiver.sample(tws, handler);
    (new Thread(ts)).start();

### Example 2:

    Collection<String> tracks = new ArrayList<String>();
    tracks.add("twitter");
    TwitterStream ts = TweetRiver.filter(tws, handler, null, tracks);
    (new Thread(ts)).start();

### TwitterStreamHandler(s)

similarly simple:

    class MyTwitterHandler implements TwitterStreamHandler {

        public void addTweet(STweet t) {
            System.out.println("TWEET! " + t.toString());
        }

        public void addLimit(STweet l) {
            System.out.println("LIMIT! " + l.toString());
        }

        public void addDeletion(SDeletion d) {
            System.out.println("DELETION! " + d.toString());
        }
        
        public void stop() {
            System.out.println("Stopped.");
        }

    }

---

### Install

* Getting Started - <http://techxplorer.com/2010/08/02/exploring-tweetstream4j/>

---

### Contributors

* John Crepezzi
* Dan Frankowski
* Sean Scanion

---

### Full Dependency List

* Gson 1.4
* Commons Logger
* Commons HttpClient 4
* Signpost (commons 4)


---

### License (BSD)

Copyright © 2010 John Crepezzi. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY John Crepezzi "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
