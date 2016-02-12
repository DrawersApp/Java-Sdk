# Java-dictionary.bot.Bot
[![Build Status](https://travis-ci.org/DrawersApp/Java-Bot.svg?branch=master)](https://travis-ci.org/DrawersApp/Java-Bot)

It is using Smack client to connect to Drawers. It is a simple echo bot which returns bot says. Replace it with your custom logic. Just override generatesreply in BotCaller. 

Install maven - sudo-apt get install maven for Ubuntu. For other platform and in general  -https://maven.apache.org/install.html
```
1. mvn install.
2. Update your username and pwd in BotCaller.
3. mvn clean package.
4. Go to target and run the fat jar using java -jar. 
```

## Awesome libraries
```
1. For writing bots with rest libraries- Retrofit - http://square.github.io/retrofit/
2. For recurring tasks like updated cricket score every 5 mins - https://quartz-scheduler.org/
3. http://www.ehcache.org/ - For storing items in memory like list of cricket matches. 
```
