package task6.task6_2;

public final class Counter 
{
    private volatile int value = 0;

    public int getValue()
    {
        return this.value;
    }

    public synchronized void increment()
    {
        ++this.value;
    }

    public synchronized void decrement()
    {
        --this.value;
    }
}
