package task5.task5_1;

final class TaskThread extends Thread
{
    private final int LINES_COUNT = 100;
    private final int CHARACTERS_COUNT = 100;

    private char character;

    public TaskThread(char character) 
    {
        this.character = character;
    }

    @Override
    public void run() 
    {
        for (int i = 0; i < this.LINES_COUNT; ++i)
        {
            for (int j = 0; j < this.CHARACTERS_COUNT; ++j) 
            {
                System.out.print(this.character);
            }

            System.out.println();
        }
    }
}
