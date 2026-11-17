/**
 * Lesson 5: a singleton. Only one Logger can ever exist.
 */
public class Logger {

    private static Logger instance;      // the one and only object
    private int messagesLogged;

    // Private constructor: no other class can write "new Logger()"
    private Logger() {
        messagesLogged = 0;
    }

    // The only way to get the Logger
    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();     // created the first time it's asked for
        }
        return instance;
    }

    public void log(String message) {
        messagesLogged++;
        System.out.println("[LOG " + messagesLogged + "] " + message);
    }
}
