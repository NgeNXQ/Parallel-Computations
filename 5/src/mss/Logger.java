package mss;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

public final class Logger extends Thread
{
    private static final int SCHEDULER_PERIOD = 1;

    private static Logger instance = null;

    private final BlockingQueue<String> MESSAGES_BUFFER;
    private final ScheduledExecutorService MESSAGES_SCHEDULER;

    private boolean isRunning;

    private Logger()
    {
        this.isRunning = true;
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
            while (!this.MESSAGES_BUFFER.isEmpty())
            {
                try
                {
                    System.out.println(this.MESSAGES_BUFFER.take());
                }
                catch (InterruptedException exception)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }, 0, Logger.SCHEDULER_PERIOD, TimeUnit.SECONDS);
    }

    public void logMessageScheduled(String message)
    {
        if (!this.isRunning)
        {
            throw new IllegalStateException("Logger is not running");
        }

        this.MESSAGES_BUFFER.offer(message);
    }

    public void logMessageInstant(String message)
    {
        if (!this.isRunning)
        {
            throw new IllegalStateException("Logger is not running");
        }

        System.out.println(message);
    }

    public void shutdown()
    {
        this.isRunning = false;
        this.MESSAGES_BUFFER.clear();
        this.MESSAGES_SCHEDULER.shutdown();
    }
}
