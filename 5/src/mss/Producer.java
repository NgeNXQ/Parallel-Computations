package mss;

public final class Producer implements Runnable
{
    public static final Task INTERRUPT_VALUE = null;

    private static final long DELAY_SIMULATION_MIN_MS = 1000;
    private static final long DELAY_SIMULATION_MAX_MS = 5000;

    private final int TASKS_COUNT;
    private final TaskBuffer TASK_BUFFER;

    public Producer(TaskBuffer taskBuffer, int tasksCount)
    {
        this.TASKS_COUNT = tasksCount;
        this.TASK_BUFFER = taskBuffer; 
    }

    @Override
    public void run()
    {
        for (int i = 0; i < this.TASKS_COUNT; ++i)
        {
            this.TASK_BUFFER.put(new Task());

            try
            {
                Thread.sleep((long)(Math.random() * (Producer.DELAY_SIMULATION_MAX_MS - Producer.DELAY_SIMULATION_MIN_MS + 1)) + Producer.DELAY_SIMULATION_MIN_MS);
            }
            catch (InterruptedException exception)
            {
                System.out.println(exception.getStackTrace());
            }
        }

        this.TASK_BUFFER.put(Producer.INTERRUPT_VALUE);
    }
}
