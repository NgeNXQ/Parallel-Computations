package mss;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

final class Channel
{
    private final ExecutorService EXECUTOR;

    private boolean isBusy;

    public Channel()
    {
        this.isBusy = false;
        this.EXECUTOR = Executors.newSingleThreadExecutor();
    }

    public boolean getIsBusy()
    {
        return this.isBusy;
    }

    public void executeTask(Task task)
    {
        this.EXECUTOR.execute(() -> 
        {
            this.isBusy = true;
            Logger.getInstance().logMessageInstant(String.format("Task #%d is in process.", task.getId()));

            task.run();

            this.isBusy = false;
            Logger.getInstance().logMessageInstant(String.format("Task #%d is finished.", task.getId()));
        });
    }

    public void shutdown()
    {
        this.isBusy = true;
        this.EXECUTOR.shutdown();
    }
}
