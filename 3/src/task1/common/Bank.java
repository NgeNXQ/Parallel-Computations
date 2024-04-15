package task1.common;

import java.util.concurrent.locks.ReentrantLock;

public final class Bank
{
    public static final int NTEST = 10000;

    private final int[] accounts;

    private long transactionsCount;
    private ReentrantLock lock = new ReentrantLock();

    public Bank(int accountsCount, int initialBalance)
    {
        this.accounts = new int[accountsCount];

        for (int i = 0; i < accounts.length; ++i)
        {
            this.accounts[i] = initialBalance;
        }

        this.transactionsCount = 0;
    }

    public int getAccountsCount()
    {
        return this.accounts.length;
    }

    public void transferDefault(int senderIndex, int receiverIndex, int amount)
    {
        this.accounts[senderIndex] -= amount;
        this.accounts[receiverIndex] += amount;

        ++this.transactionsCount;

        if (this.transactionsCount % NTEST == 0)
        {
            test();
        }
    }

    public void transferWithLock(int senderIndex, int receiverIndex, int amount)
    {
        this.lock.lock();

        try
        {
            this.accounts[senderIndex] -= amount;
            this.accounts[receiverIndex] += amount;
    
            ++this.transactionsCount;
    
            if (this.transactionsCount % NTEST == 0)
            {
                test();
            }
        }
        finally
        {
            this.lock.unlock();
        }
    }

    public void transferWithSynchronizedBlock(int senderIndex, int receiverIndex, int amount)
    {
        synchronized(this)
        {
            this.accounts[senderIndex] -= amount;
            this.accounts[receiverIndex] += amount;
    
            ++this.transactionsCount;
    
            if (this.transactionsCount % NTEST == 0)
            {
                test();
            }
        }
    }

    public synchronized void transferWithSynchronizedMethod(int senderIndex, int receiverIndex, int amount)
    {
        this.accounts[senderIndex] -= amount;
        this.accounts[receiverIndex] += amount;

        ++this.transactionsCount;

        if (this.transactionsCount % NTEST == 0)
        {
            test();
        }
    }

    public void test()
    {
        int sum = 0;
        
        for (int i = 0; i < accounts.length; i++)
        {
            sum += accounts[i];
        }

        System.out.println("Transactions:" + this.transactionsCount + " Sum: " + sum);
    }
}
