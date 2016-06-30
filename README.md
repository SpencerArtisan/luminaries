# luminaries
A terminal-based twitter client.  Filters out the noise to show the most interesting information from the most interesting people.

## Getting started

You need a twitter account with a developer App for which you have generated a Consumer Key and Secret

1. mvn install
1. alias news='java -Dtwitter4j.oauth.consumerKey=[YOUR CONSUMER KEY] -Dtwitter4j.oauth.consumerSecret=[YOUR CONSUMER SECRET] -cp [ABSOLTE PATH TO target/luminaries-1.0-SNAPSHOT-jar-with-dependencies.jar] com.artisansoftware.luminaries.App'
1. news [number of hours back]


