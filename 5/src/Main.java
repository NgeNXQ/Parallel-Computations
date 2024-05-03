import mss.*;

final class Main
{
    public static void main(String[] args)
    {
        final int SIMULATIONS = 20;
        final int BUFFER_SIZE = 1000;
        final int MODEL_CHANNELS_COUNT = 3;
        final int MODEL_MAX_QUEUE_LENGTH = 5;

        Logger.getInstance().start();

        for (int i = 0; i < SIMULATIONS; ++i)
        {
            final TaskBuffer TASK_BUFFER = new TaskBuffer();
            final Producer PRODUCER = new Producer(TASK_BUFFER, BUFFER_SIZE);
            final ModelConsumer CONSUMER = new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH, TASK_BUFFER);

            final Thread PRODUCER_THREAD = new Thread(PRODUCER);
            final Thread CONSUMER_THREAD = new Thread(CONSUMER);

            PRODUCER_THREAD.start();
            CONSUMER_THREAD.start();

            try
            {
                PRODUCER_THREAD.join();
                CONSUMER_THREAD.join();
            }
            catch (InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }

        Logger.getInstance().shutdown();
    }
}
