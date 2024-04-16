package task1.task1_4;

import task1.common.Bank;

final class Main
{
    public static void main(String[] args)
    {
        final int ACCOUNTS_COUNT = 10;
        final int INITIAL_BALANCE = 10000;

        Bank bank = new Bank(ACCOUNTS_COUNT, INITIAL_BALANCE);
        
        for (int i = 0; i < ACCOUNTS_COUNT; ++i)
        {
            TransferThreadSynchronizedMethod transferThread = new TransferThreadSynchronizedMethod(bank, i, INITIAL_BALANCE);
            transferThread.setPriority(Thread.NORM_PRIORITY + i % 2);
            transferThread.start();
        }
    }
}
