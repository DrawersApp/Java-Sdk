package drawers

/**
 * Created by nishant.pathak on 04/04/16.
 */


fun main(args: Array<String>) {
    val botsCaller = Subscriber()
    Bot.getBot(botsCaller, "uname", "pwd")
    while (true) {
        Thread.sleep(1000000000000L)
    }

}