package task2;

final class Main
{
    public static void main(String[] args)
    {
        final int BUFFER_SIZE = 100;

        Drop drop = new Drop();

        (new Thread(new Producer(drop, BUFFER_SIZE))).start();

        (new Thread(new Consumer(drop))).start();
    }
}