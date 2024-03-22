package task5.task5_2;

final class TaskThread extends Thread 
{
    private static volatile int linesCount = 0;

    private final int LINES_COUNT = 100;
    private final int CHARACTERS_COUNT = 100;

    private Object lock;
    private char character;

    public TaskThread(char character, Object lock) 
    {
        this.lock = lock;
        this.character = character;
    }

    @Override
    public void run()
    {
        synchronized (this.lock) 
        {
            try
            {
                while (TaskThread.linesCount <= LINES_COUNT)
                {
                    for (int i = 0; i < CHARACTERS_COUNT; ++i)
                    {
                        System.out.print(this.character);
                    }

                    ++TaskThread.linesCount;

                    System.out.println();

                    this.lock.notify();

                    this.lock.wait();
                }

                this.lock.notify();
            }
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
    }
}