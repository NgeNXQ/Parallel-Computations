package task3;

import java.lang.InterruptedException;

public final class BallThread extends Thread 
{
    private final int ITERATIONS_COUNT = 10000;
    private final int THREAD_SLEEP_DURATION_MS = 5;

    private Ball ball;

    public BallThread(Ball ball)
    {
        this.ball = ball;
    }

    @Override
    public void run()
    {
        try
        {
            for(int i = 1; i < this.ITERATIONS_COUNT; ++i)
            {
                ball.move();

                System.out.println("Thread name = " + Thread.currentThread().getName());

                Thread.sleep(this.THREAD_SLEEP_DURATION_MS);
            }
        }
        catch(InterruptedException ex) { }
    }
}