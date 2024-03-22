package task6.task6_1;

final class Main
{
    public static void main(String[] args)
    {
        final int ITERATIONS_COUNT = 100000;

        Counter counter = new Counter();

        Thread incrementThread = new Thread(() -> 
        {
            for (int i = 0; i < ITERATIONS_COUNT; ++i) 
            {
                counter.increment();
            }

            System.out.println("Increment Value: " + counter.getValue());
        });

        Thread decrementThread = new Thread(() -> 
        {
            for (int i = 0; i < ITERATIONS_COUNT; ++i)
            {
                counter.decrement();
            }

            System.out.println("Decrement Value: " + counter.getValue());
        });

        incrementThread.start();
        decrementThread.start();

        System.out.println("Total Value: " + counter.getValue());
    }
}