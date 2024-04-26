package mss;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

public final class Logger extends Thread
{
    private static final long SCHEDULER_PERIOD = 10;

    private static Logger instance = null;

    private final BlockingQueue<String> MESSAGES_BUFFER;
    private final ScheduledExecutorService MESSAGES_SCHEDULER;

    private Logger()
    {
        this.MESSAGES_BUFFER = new LinkedBlockingQueue<>();
        this.MESSAGES_SCHEDULER = Executors.newScheduledThreadPool(1);
    }

    public static synchronized Logger getInstance()
    {
        if (Logger.instance == null)
        {
            Logger.instance = new Logger();
        }

        return Logger.instance;
    }

    @Override
    public void run()
    {
        this.MESSAGES_SCHEDULER.scheduleAtFixedRate(() -> 
        {
            String message = MESSAGES_BUFFER.poll();

            if (message != null)
            {
                System.out.println(message);
            }

        }, 0, Logger.SCHEDULER_PERIOD, TimeUnit.SECONDS);
    }

    public void logMessageScheduled(String message)
    {
        this.MESSAGES_BUFFER.offer(message);
    }

    public void logMessageInstant(String message)
    {
        System.out.println(message);
    }

    public void shutdown()
    {
        this.MESSAGES_BUFFER.clear();
        this.MESSAGES_SCHEDULER.shutdown();
    }
}
