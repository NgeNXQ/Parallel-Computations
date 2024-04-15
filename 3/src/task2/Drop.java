package task2;

final class Drop 
{
    private int value; // Message sent from producer to consumer.

    // True if consumer should wait for producer to send message,
    // False if producer should wait for consumer to retrieve message.
    private boolean isEmpty = true;

    public synchronized int take()
    {
        while (this.isEmpty)    // Wait until message is available.
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        this.isEmpty = true;    // Toggle status.

        this.notifyAll();       // Notify producer that status has changed.
    
        return this.value;
    }

    public synchronized void put(int value)
    {
        while (!this.isEmpty)   // Wait until message has been retrieved.
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        this.isEmpty = false;   // Toggle status.

        this.value = value;     // Store message.

        this.notifyAll();       // Notify consumer that status has changed.
    }
}
