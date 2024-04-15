package task1.task1_2;

import task1.common.Bank;

final class TransferThreadLock extends Thread
{
    private static final int REPS = 1000;

    private Bank bank;

    private int maxAmount;
    private int senderIndex;

    public TransferThreadLock(Bank bank, int senderIndex, int maxAmount)
    {
        this.bank = bank;
        this.maxAmount = maxAmount;
        this.senderIndex = senderIndex;
    }

    @Override
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < TransferThreadLock.REPS; ++i)
            {
                int receiverIndex = (int) (this.bank.getAccountsCount() * Math.random());
                int amount = (int) (this.maxAmount * Math.random() / TransferThreadLock.REPS);

                this.bank.transferWithLock(this.senderIndex, receiverIndex, amount);
            }
        }
    }
}
