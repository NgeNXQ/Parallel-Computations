package task2;

import java.lang.InterruptedException;

public final class BallThread extends Thread 
{
    private final int ITERATIONS_COUNT = 10000;
    private final int THREAD_SLEEP_DURATION_MS = 5;

    private Ball ball;
    private Canvas canvas;

    public BallThread(Ball ball, Canvas canvas)
    {
        this.ball = ball;
        this.canvas = canvas;
    }

    @Override
    public void run()
    {
        try
        {
            for(int i = 1; i < this.ITERATIONS_COUNT; ++i)
            {
                ball.move();

                if (this.canvas.hasCollided(ball))
                {
                    this.interrupt();
                    System.out.println("Thread name = " + Thread.currentThread().getName() + ". Status: " + Thread.currentThread().getState());
                    break;
                }

                System.out.println("Thread name = " + Thread.currentThread().getName());

                Thread.sleep(this.THREAD_SLEEP_DURATION_MS);
            }
        }
        catch(InterruptedException ex) { }
    }
}