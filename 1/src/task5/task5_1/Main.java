package task5.task5_1;

final class Main
{
    public static void main(String[] args)
    {
        Thread thread1 = new TaskThread('-');
        Thread thread2 = new TaskThread('|');

        thread1.start();
        thread2.start();

        try
        {
            thread1.join();
            thread2.join();
        }
        catch (InterruptedException ex)
        {

        }
    }
}