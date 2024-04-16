package task2;

import java.util.Random;

final class Producer implements Runnable
{
    public static final int INTERRUPT_VALUE = -1;

    private Drop drop;
    private int[] dataBuffer;

    public Producer(Drop drop, int dataBufferSize)
    {
        this.drop = drop;
        this.dataBuffer = new int[dataBufferSize];
    }

    public void run()
    {
        Random random = new Random();

        for (int i = 0; i < this.dataBuffer.length; ++i)
        {
            this.dataBuffer[i] = i;

            this.drop.put(this.dataBuffer[i]);

            try
            {
                Thread.sleep(random.nextInt(5000));
            }
            catch (InterruptedException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        this.drop.put(Producer.INTERRUPT_VALUE);
    }
}
