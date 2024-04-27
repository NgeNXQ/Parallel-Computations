package mss;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

final class Channel
{
    private static int nextId = 0;

    private final int ID;
    private final int MODEL_ID;
    private final ExecutorService EXECUTOR;

    private boolean isBusy;

    public Channel(int modelId)
    {
        this.isBusy = false;
        this.MODEL_ID = modelId;
        this.ID = ++Channel.nextId;
        this.EXECUTOR = Executors.newSingleThreadExecutor();
    }

    public int getId()
    {
        return this.ID;
    }

    public int getModelId()
    {
        return this.MODEL_ID;
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
            Logger.getInstance().logMessageInstant(String.format("MODEL #%d LOG | TASK #%d is in process on Channel #%d.", this.MODEL_ID, task.getId(), this.ID));

            task.run();

            this.isBusy = false;
            Logger.getInstance().logMessageInstant(String.format("MODEL #%d LOG | TASK #%d is finished on Channel #%d.", this.MODEL_ID, task.getId(), this.ID));
        });
    }

    public void shutdown()
    {
        this.isBusy = true;
        this.EXECUTOR.shutdown();
    }
}
