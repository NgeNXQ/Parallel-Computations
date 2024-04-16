package task1.task1_3;

import task1.common.Bank;

final class TransferThreadSynchronizedBlock extends Thread
{
    private static final int REPS = 1000;

    private Bank bank;

    private int maxAmount;
    private int senderIndex;

    public TransferThreadSynchronizedBlock(Bank bank, int senderIndex, int maxAmount)
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
            for (int i = 0; i < TransferThreadSynchronizedBlock.REPS; ++i)
            {
                int receiverIndex = (int) (this.bank.getAccountsCount() * Math.random());
                int amount = (int) (this.maxAmount * Math.random() / TransferThreadSynchronizedBlock.REPS);

                this.bank.transferWithSynchronizedBlock(this.senderIndex, receiverIndex, amount);
            }
        }
    }
}
