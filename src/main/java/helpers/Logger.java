package helpers;

public class Logger {

    private static final ThreadLocal<org.apache.log4j.Logger> threadLocal = new ThreadLocal<>();

    public static final ThreadLocal<StringBuffer> flushedLogs = new ThreadLocal<>();

    protected static org.apache.log4j.Logger getLogger() {
        if (threadLocal.get() == null) {
            threadLocal.set(org.apache.log4j.Logger.getLogger("Thread#" + Thread.currentThread().getId()));
            flushedLogs.set(new StringBuffer());
        }
        return threadLocal.get();
    }

    public static void info(Object message) {
        getLogger().info(message);
        appendToBufferLogs(message, " INFO ");
    }

    public static void warn(Object message) {
        appendToBufferLogs(message, " WARNING ");
    }

    public static void error(Object message) {
        appendToBufferLogs(message, " ERROR ");
    }

    private static void appendToBufferLogs(Object message, String logType) {
        flushedLogs.get()
                .append("\n")
                .append(logType)
                .append("Thread#")
                .append(Thread.currentThread().getId())
                .append(" - ")
                .append(message);
    }

    public static String getTestLogs() {
        return flushedLogs.get().toString();
    }
}
