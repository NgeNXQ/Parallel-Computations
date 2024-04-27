package mss;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class ModelConsumer implements Runnable
{
    private static int nextId = 0;

    private final int ID;
    private final Channel[] POOL;
    private final TaskBuffer BUFFER;
    private final BlockingQueue<Task> PENDING_TASKS;
    private final LinkedList<Integer> PENDING_TASKS_DUMPS;

    private AtomicInteger totalRefusedTasks;
    private AtomicInteger totalSubmittedTasks;
    private AtomicInteger totalCompletedTasks;

    public ModelConsumer(int channelsCount, int maxQueueLength, TaskBuffer buffer)
    {
        this.ID = ++ModelConsumer.nextId;
        this.totalRefusedTasks = new AtomicInteger(0);
        this.totalSubmittedTasks = new AtomicInteger(0);
        this.totalCompletedTasks = new AtomicInteger(0);

        this.BUFFER = buffer;
        this.POOL = new Channel[channelsCount];
        this.PENDING_TASKS_DUMPS = new LinkedList<>();
        this.PENDING_TASKS = new ArrayBlockingQueue<>(maxQueueLength);

        for (int i = 0; i < channelsCount; ++i)
        {
            this.POOL[i] = new Channel(this.ID);
        }
    }

    public int getId()
    {
        return this.ID;
    }

    public double getMeanQueueSize()
    {
        double sum = 0.0;

        for (int number : this.PENDING_TASKS_DUMPS)
        {
            sum += number;
        }

        return sum / this.PENDING_TASKS_DUMPS.size();
    }

    public int getTotalRefusedTasks()
    {
        return this.totalRefusedTasks.get();
    }

    public int getTotalSubmittedTasks()
    {
        return this.totalSubmittedTasks.get();
    }

    public int getTotalCompletedTasks()
    {
        return this.totalCompletedTasks.get();
    }

    public double getRefusalProbability()
    {
        return (double)this.totalRefusedTasks.get() / this.totalSubmittedTasks.get();
    }

    @Override
    public void run()
    {
        final int INDEX_LAST_CHANNEL = POOL.length - 1;

        boolean hasFinished = false;
        Task task = this.BUFFER.take();

        while (task != Producer.INTERRUPT_VALUE)
        {
            this.totalSubmittedTasks.incrementAndGet();

            for (int i = 0; i < POOL.length; ++i)
            {
                if (!this.POOL[i].getIsBusy())
                {
                    this.totalCompletedTasks.incrementAndGet();
                    this.POOL[i].executeTask(task);
                    break;
                }

                if (i == INDEX_LAST_CHANNEL && !this.PENDING_TASKS.offer(task))
                {
                    this.totalRefusedTasks.incrementAndGet();
                    Logger.getInstance().logMessageInstant(String.format("MODEL #%d LOG | TASK #%d is refused.", this.ID, task.getId()));
                }
            }

            if (!hasFinished)
            {
                task = this.BUFFER.take();
                hasFinished = task == Producer.INTERRUPT_VALUE ? true : false;
            }
            else
            {
                task = Producer.INTERRUPT_VALUE;
            }

            if (!this.PENDING_TASKS.isEmpty())
            {
                if (!hasFinished && !this.PENDING_TASKS.offer(task))
                {
                    this.totalRefusedTasks.incrementAndGet();
                    Logger.getInstance().logMessageInstant(String.format("MODEL #%d LOG | TASK #%d is refused.", this.ID, task.getId()));
                }
                else
                {
                    task = this.PENDING_TASKS.poll();
                }
            }

            this.PENDING_TASKS_DUMPS.add(this.PENDING_TASKS.size());

            Logger.getInstance().logMessageScheduled(Thread.currentThread(), String.format("MODEL #%d SCHEDULED LOG | Submitted: %d; Completed: %d; Refused: %d.", this.ID, this.totalSubmittedTasks.get(), this.totalCompletedTasks.get(), this.totalRefusedTasks.get()));
        }

        for (Channel channel : this.POOL)
        {
            channel.shutdown();
        }

        Logger.getInstance().logMessageInstant(String.format("MODEL #%d REPORT | Refusal probability: %.3f; Queue's mean size: %.3f.", this.ID, this.getRefusalProbability(), this.getMeanQueueSize()));
    }
}
