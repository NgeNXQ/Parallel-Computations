import mss.*;

final class Main
{
    public static void main(String[] args)
    {
        final int BUFFER_SIZE = 1000;
        final int MODEL_CHANNELS_COUNT = 5;
        final int MODEL_MAX_QUEUE_LENGTH = 10;

        final TaskBuffer TASK_BUFFER_1 = new TaskBuffer();
        final TaskBuffer TASK_BUFFER_2 = new TaskBuffer();
        final TaskBuffer TASK_BUFFER_3 = new TaskBuffer();
        final TaskBuffer TASK_BUFFER_4 = new TaskBuffer();

        final Producer PRODUCER_1 = new Producer(TASK_BUFFER_1, BUFFER_SIZE);
        final Producer PRODUCER_2 = new Producer(TASK_BUFFER_2, BUFFER_SIZE);
        final Producer PRODUCER_3 = new Producer(TASK_BUFFER_3, BUFFER_SIZE);
        final Producer PRODUCER_4 = new Producer(TASK_BUFFER_4, BUFFER_SIZE);

        final ModelConsumer CONSUMER_1 = new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH, TASK_BUFFER_1);
        final ModelConsumer CONSUMER_2 = new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH, TASK_BUFFER_2);
        final ModelConsumer CONSUMER_3 = new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH, TASK_BUFFER_3);
        final ModelConsumer CONSUMER_4 = new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH, TASK_BUFFER_4);

        final Thread PRODUCER_THREAD_1 = new Thread(PRODUCER_1);
        final Thread PRODUCER_THREAD_2 = new Thread(PRODUCER_2);
        final Thread PRODUCER_THREAD_3 = new Thread(PRODUCER_3);
        final Thread PRODUCER_THREAD_4 = new Thread(PRODUCER_4);

        final Thread CONSUMER_THREAD_1 = new Thread(CONSUMER_1);
        final Thread CONSUMER_THREAD_2 = new Thread(CONSUMER_2);
        final Thread CONSUMER_THREAD_3 = new Thread(CONSUMER_3);
        final Thread CONSUMER_THREAD_4 = new Thread(CONSUMER_4);

        Logger.getInstance().start();

        PRODUCER_THREAD_1.start();
        PRODUCER_THREAD_2.start();
        PRODUCER_THREAD_3.start();
        PRODUCER_THREAD_4.start();

        CONSUMER_THREAD_1.start();
        CONSUMER_THREAD_2.start();
        CONSUMER_THREAD_3.start();
        CONSUMER_THREAD_4.start();

        try
        {
            PRODUCER_THREAD_1.join();
            PRODUCER_THREAD_2.join();
            PRODUCER_THREAD_3.join();
            PRODUCER_THREAD_4.join();
            CONSUMER_THREAD_1.join();
            CONSUMER_THREAD_2.join();
            CONSUMER_THREAD_3.join();
            CONSUMER_THREAD_4.join();
        }
        catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }

        Logger.getInstance().shutdown();
    }
}
