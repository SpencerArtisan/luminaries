# luminaries
A terminal-based twitter client.  Filters out the noise to show the most interesting information from the most interesting people.

## Getting started

You need a twitter account with a developer App for which you have generated a Consumer and App keys
The config data is stored in a json file, saved to the user's home directory.

This is intended to be run from the terminal. Suggest setting up an alias with a sensible name (like "news") in your .bashrc file.

First, build the code:
<pre>
mvn install
</pre>

Next, create your .bashrc alias:
<pre>
``alias news='java
[-Dcom.artisansoftware.luminaries.file=[config file name (defaults to luminaries)]
-Dtwitter4j.oauth.accessToken=[YOUR ACCESS KEY]
-Dtwitter4j.oauth.accessTokenSecret=[YOUR ACCESS TOKEN SECRET]
-Dtwitter4j.oauth.consumerKey=[YOUR CONSUMER KEY]
-Dtwitter4j.oauth.consumerSecret=[YOUR CONSUMER SECRET]
-jar [ABSOLTE PATH TO target/luminaries-1.0-SNAPSHOT-jar-with-dependencies.jar]'``
</pre>

Now, try it out:
<pre>
news -h
</pre>

