package mss;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class ModelConsumer implements Runnable
{
    private final Buffer BUFFER;
    private final Channel[] CHANNELS_POOL;
    private final BlockingQueue<Task> QUEUE;

    private AtomicInteger totalRefusedTasks;
    private AtomicInteger totalSubmittedTasks;
    private AtomicInteger totalCompletedTasks;

    public ModelConsumer(int channelsCount, int maxQueueLength)
    {
        this.totalRefusedTasks = new AtomicInteger(0);
        this.totalSubmittedTasks = new AtomicInteger(0);
        this.totalCompletedTasks = new AtomicInteger(0);

        this.BUFFER = new Buffer();
        this.CHANNELS_POOL = new Channel[channelsCount];
        this.QUEUE = new ArrayBlockingQueue<>(maxQueueLength);

        for (int i = 0; i < channelsCount; ++i)
        {
            this.CHANNELS_POOL[i] = new Channel();
        }
    }

    public double getAverageQueueLength()
    {
        return (double) this.totalSubmittedTasks.get() / (this.totalSubmittedTasks.get() - this.totalRefusedTasks.get());
    }

    public double getRefusalProbability()
    {
        return (double) this.totalRefusedTasks.get() / this.totalSubmittedTasks.get();
    }

    @Override
    public void run()
    {
        final int INDEX_LAST_CHANNEL = CHANNELS_POOL.length - 1;

        Task task = this.BUFFER.take();

        while (task != Producer.INTERRUPT_VALUE)
        {
            this.totalSubmittedTasks.incrementAndGet();

            for (int i = 0; i < CHANNELS_POOL.length; ++i)
            {
                if (!this.CHANNELS_POOL[i].getIsBusy())
                {
                    this.CHANNELS_POOL[i].executeTask(task);
                    this.totalCompletedTasks.incrementAndGet();
                    break;
                }

                if (i == INDEX_LAST_CHANNEL)
                {
                    if (!this.QUEUE.offer(task))
                    {
                        this.totalRefusedTasks.incrementAndGet();
                    }
                }
            }

            task = this.QUEUE.isEmpty() ? this.BUFFER.take() : this.QUEUE.poll();
        }

        for (Channel channel : this.CHANNELS_POOL)
        {
            channel.shutdown();
        }
    }
}
