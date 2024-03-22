package task6.task6_3;

public final class Counter 
{
    private volatile int value = 0;

    public int getValue()
    {
        return this.value;
    }

    public void increment()
    {
        synchronized(this)
        {
            ++this.value;
        }
    }

    public void decrement()
    {
        synchronized(this)
        {
            --this.value;
        }
    }
}
