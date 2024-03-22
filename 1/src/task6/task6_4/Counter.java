package task6.task6_4;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class Counter 
{
    private volatile int value = 0;

    private Lock lock = new ReentrantLock();

    public int getValue()
    {
        return this.value;
    }

    public void increment()
    {
        this.lock.lock();

        try 
        {
            ++this.value;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void decrement()
    {
        this.lock.lock();

        try 
        {
            --this.value;
        }
        finally
        {
            lock.unlock();
        }
    }
}
