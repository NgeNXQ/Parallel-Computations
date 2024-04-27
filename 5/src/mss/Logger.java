package mss;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;

public final class Logger
{
    private static final Logger INSTANCE = new Logger();

    private static final long SCHEDULER_PERIOD = 10;

    private final ScheduledExecutorService MESSAGES_SCHEDULER;
    private final ConcurrentHashMap<Thread, LinkedBlockingDeque<String>> MESSAGES_BUFFERS;

    private Logger()
    {
        this.MESSAGES_BUFFERS = new ConcurrentHashMap<>();
        this.MESSAGES_SCHEDULER = Executors.newScheduledThreadPool(1);
    }

    public static Logger getInstance()
    {
        return Logger.INSTANCE;
    }

    public synchronized void start()
    {
        this.MESSAGES_BUFFERS.clear();
        this.MESSAGES_SCHEDULER.scheduleAtFixedRate(() -> 
        {
            for (final Thread THREAD : this.MESSAGES_BUFFERS.keySet())
            {
                final String MESSAGE = MESSAGES_BUFFERS.get(THREAD).pollLast();

                if (MESSAGE != null)
                {
                    System.out.println(MESSAGE);
                    this.MESSAGES_BUFFERS.get(THREAD).clear();
                }
            }
        }, 0, Logger.SCHEDULER_PERIOD, TimeUnit.SECONDS);
    }

    public synchronized void logMessageInstant(String message)
    {
        System.out.println(message);
    }

    public synchronized void logMessageScheduled(Thread thread, String message)
    {
        this.MESSAGES_BUFFERS.computeIfAbsent(thread, k -> new LinkedBlockingDeque<>()).offer(message);
    }

    public synchronized void shutdown()
    {
        this.MESSAGES_BUFFERS.clear();
        this.MESSAGES_SCHEDULER.shutdown();
    }
}
