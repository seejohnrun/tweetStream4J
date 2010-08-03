# TweetStream4J

*This is a Java binding for the Twitter API written in a way that will allow Java developers to quickly and successfully work with the Twitter API.*  
*It is in complete working order, but there are some definite issues as it is only one day old.  See the Issues tab for more info*

---

### Example 1:

    TwitterStreamConfiguraion tws = new TwitterStreamConfiguration("username", "password");
    MyTwitterHandler handler = new MyTwitterHandler();

    TwitterStream ts = TweetRiver.sample(tws, handler);
    (new Thread(ts)).start();

### Example 2:

    TwitterStreamConfiguration tws = new TwitterStreamConfiguration("username", "password");
    MyTwitterHandler handler = new MyTwitterHandler();

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

* Getting Started - http://techxplorer.com/2010/08/02/exploring-tweetstream4j/

---

### Contributors

* John Crepezzi
* Dan Frankowski
* Sean Scanion

---

### Full Dependency List

* JSON-LIB 2.3
* EZMorph 1.0.6
* Commons - *BeanUtils*
* Commons - *Lang*
* Commons - *Collections*
* Commons - *Logging*

---

### Maven json-lib dependency 

for some reason json lib artifacts are deployed prefixed with the jdk version they're compatilble with, which makes the build fail  
here is a work-around to install it into your local repo:

* curl -O http://repo1.maven.org/maven2/net/sf/json-lib/json-lib/2.2.3/json-lib-2.2.3-jdk15.jar
  
* mvn install:install-file -DgroupId=net.sf.json-lib -DartifactId=json-lib -Dversion=2.2.3 -Dpackaging=jar -Dfile=json-lib-2.2.3-jdk15.jar
    rm json-lib-2.2.3-jdk15.jar

---

### License (BSD)

Copyright © 2010 John Crepezzi. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY John Crepezzi "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.