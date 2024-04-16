package task1.task1_1;

import task1.common.Bank;

final class TransferThread extends Thread
{
    private static final int REPS = 1000;

    private Bank bank;

    private int maxAmount;
    private int senderIndex;

    public TransferThread(Bank bank, int senderIndex, int maxAmount)
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
            for (int i = 0; i < TransferThread.REPS; ++i)
            {
                int receiverIndex = (int) (this.bank.getAccountsCount() * Math.random());
                int amount = (int) (this.maxAmount * Math.random() / TransferThread.REPS);

                this.bank.transferDefault(this.senderIndex, receiverIndex, amount);
            }
        }
    }
}
