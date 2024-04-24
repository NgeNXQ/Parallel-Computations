import mss.*;

final class Main
{
    public static void main(String[] args)
    {
        final int BUFFER_SIZE = 1000;
        final int MODEL_CHANNELS_COUNT = 3;
        final int MODEL_MAX_QUEUE_LENGTH = 10;

        Thread producerThread = new Thread(new Producer(BUFFER_SIZE));
        Thread consumerThread = new Thread(new ModelConsumer(MODEL_CHANNELS_COUNT, MODEL_MAX_QUEUE_LENGTH));

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
    
        //Logger.getInstance().start();
//
        //Model model = new Model(2);

        //for (int i = 0; i < 1; ++i)
        //{
        //    model.submitTask(new Task());
        //}

        //model.start();

        //System.out.println("Average Queue Length: " + model.getAverageQueueLength());
       // System.out.println("Refusal Probability: " + model.getRefusalProbability());

       // Logger.getInstance().shutdown();
    }
}
