package mss;

final class Task implements Runnable
{
    private static final long DELAY_SIMULATION_MIN_MS = 1000;
    private static final long DELAY_SIMULATION_MAX_MS = 10000;

    private static int nextId = 0;

    private final int ID;

    public Task()
    {
        this.ID = ++Task.nextId;
    }

    public int getId()
    {
        return this.ID;
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep((long)(Math.random() * (Task.DELAY_SIMULATION_MAX_MS - Task.DELAY_SIMULATION_MIN_MS + 1)) + Task.DELAY_SIMULATION_MIN_MS);
        }
        catch (InterruptedException exception)
        {
            //Thread.currentThread().interrupt();
            System.out.println(exception.getStackTrace());
        }
    }
}
