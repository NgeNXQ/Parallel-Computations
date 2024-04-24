package mss;

public final class Producer implements Runnable
{
    public static final Task INTERRUPT_VALUE = null;

    private static final long DELAY_SIMULATION_MIN_MS = 1000;
    private static final long DELAY_SIMULATION_MAX_MS = 10000;

    private final Buffer BUFFER;
    private final int TASKS_COUNT;

    public Producer(int tasksCount)
    {
        this.BUFFER = new Buffer(); 
        this.TASKS_COUNT = tasksCount;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < this.TASKS_COUNT; ++i)
        {
            this.BUFFER.put(new Task());

            try
            {
                Thread.sleep((long)(Math.random() * (Producer.DELAY_SIMULATION_MAX_MS - Producer.DELAY_SIMULATION_MIN_MS + 1)) + Producer.DELAY_SIMULATION_MIN_MS);
            }
            catch (InterruptedException exception)
            {
                System.out.println(exception.getStackTrace());
            }
        }

        this.BUFFER.put(Producer.INTERRUPT_VALUE);
    }
}
