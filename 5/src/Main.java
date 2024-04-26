import mss.*;

final class Main
{
    public static void main(String[] args)
    {
        final int BUFFER_SIZE = 10;
        final int MODEL_CHANNELS_COUNT = 3;
        final int MODEL_MAX_QUEUE_LENGTH = 10;

        Logger.getInstance().start();

        final TaskBuffer TASK_BUFFER = new TaskBuffer();
        final Producer PRODUCER = new Producer(TASK_BUFFER, BUFFER_SIZE);
        final ModelConsumer MODEL_CONSUMER = new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH, TASK_BUFFER);

        Thread producerThread = new Thread(PRODUCER);
        Thread consumerThread = new Thread(MODEL_CONSUMER);

        producerThread.start();
        consumerThread.start();

        try
        {
            producerThread.join();
            consumerThread.join();
        }
        catch (InterruptedException exception)
        {
            System.err.println(exception.getStackTrace());
        }

        Logger.getInstance().shutdown();
    }
}
