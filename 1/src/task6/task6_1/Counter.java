package task6.task6_1;

public final class Counter
{
    private volatile int value = 0;

    public int getValue()
    {
        return this.value;
    }

    public void increment()
    {
        ++this.value;
    }

    public void decrement()
    {
        --this.value;
    }
}
