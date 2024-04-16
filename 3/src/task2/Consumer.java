package task2;

import java.util.Random;

public class Consumer implements Runnable
{
    private Drop drop;

    public Consumer(Drop drop)
    {
        this.drop = drop;
    }

    public void run()
    {
        Random random = new Random();

        int value = this.drop.take();

        while (value != Producer.INTERRUPT_VALUE)
        {
            System.out.format("VALUE RECEIVED: %s%n", value);

            try
            {
                Thread.sleep(random.nextInt(5000));
            }
            catch (InterruptedException e)
            {
                System.out.println(e.getStackTrace());
            }

            value = this.drop.take();
        }
    }
}
