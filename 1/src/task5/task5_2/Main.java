package task5.task5_2;

final class Main
{
    public static void main(String[] args)
    {
        Object lock = new Object();

        Thread thread1 = new TaskThread('-', lock);
        Thread thread2 = new TaskThread('|', lock);

        thread1.start();
        thread2.start();
    }
}