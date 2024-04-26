package mss;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class ModelConsumer implements Runnable
{
    private final TaskBuffer TASK_BUFFER;
    private final Channel[] CHANNELS_POOL;
    private final BlockingQueue<Task> QUEUE;
    private final LinkedList<Integer> QUEUE_DUMPS;

    private AtomicInteger totalRefusedTasks;
    private AtomicInteger totalSubmittedTasks;
    private AtomicInteger totalCompletedTasks;

    public ModelConsumer(int channelsCount, int maxQueueLength, TaskBuffer taskBuffer)
    {
        this.totalRefusedTasks = new AtomicInteger(0);
        this.totalSubmittedTasks = new AtomicInteger(0);
        this.totalCompletedTasks = new AtomicInteger(0);

        this.TASK_BUFFER = taskBuffer;
        this.QUEUE_DUMPS = new LinkedList<>();
        this.CHANNELS_POOL = new Channel[channelsCount];
        this.QUEUE = new ArrayBlockingQueue<>(maxQueueLength);

        for (int i = 0; i < channelsCount; ++i)
        {
            this.CHANNELS_POOL[i] = new Channel();
        }
    }

    public int getRefusedTasks()
    {
        return this.totalRefusedTasks.get();
    }

    public int getSubmittedTasks()
    {
        return this.totalSubmittedTasks.get();
    }

    public int getCompletedTasks()
    {
        return this.totalCompletedTasks.get();
    }

    public double getMeanQueueSize()
    {
        double sum = 0.0;

        for (int number : this.QUEUE_DUMPS)
        {
            sum += number;
        }

        return sum / this.QUEUE_DUMPS.size();
    }

    public double getRefusalProbability()
    {
        return (double) this.totalRefusedTasks.get() / this.totalSubmittedTasks.get();
    }

    @Override
    public void run()
    {
        final int INDEX_LAST_CHANNEL = CHANNELS_POOL.length - 1;

        boolean hasFinished = false;
        Task task = this.TASK_BUFFER.take();

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

            if (!hasFinished)
            {
                task = this.TASK_BUFFER.take();
                hasFinished = task == Producer.INTERRUPT_VALUE ? true : false;
            }
            else
            {
                task = Producer.INTERRUPT_VALUE;
            }

            if (!this.QUEUE.isEmpty())
            {
                if (!hasFinished && !this.QUEUE.offer(task))
                {
                    this.totalRefusedTasks.incrementAndGet();
                }
                else
                {
                    task = this.QUEUE.poll();
                }
            }

            this.QUEUE_DUMPS.add(this.QUEUE.size());

            Logger.getInstance().logMessageScheduled(String.format("MODEL_LOG | Submitted: %d; Completed: %d; Refused: %d.", this.totalSubmittedTasks.get(), this.totalCompletedTasks.get(), this.totalRefusedTasks.get()));
        }

        for (Channel channel : this.CHANNELS_POOL)
        {
            channel.shutdown();
        }

        Logger.getInstance().logMessageInstant(String.format("MODEL_REPORT | Refusal probability: %.3f; Queue's mean size: %.3f.", this.getRefusalProbability(), this.getMeanQueueSize()));
    }
}
