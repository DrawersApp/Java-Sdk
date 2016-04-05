package drawers

import java.util.concurrent.TimeUnit

/**
 * Created by nishant.pathak on 04/04/16.
 */


fun main(args: Array<String>) {
    val botsCaller = Subscriber()
    val bot = Bot.getBot(botsCaller, "uname", "pwd")
    bot.executorService.awaitTermination(1000000000000L, TimeUnit.DAYS)
}