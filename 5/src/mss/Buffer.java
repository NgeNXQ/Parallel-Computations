package mss;

final class Buffer 
{
    private Task value;
    private boolean isEmpty = true;

    public synchronized Task take()
    {
        while (this.isEmpty)
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException exception)
            {
                System.out.println(exception.getStackTrace());
            }
        }

        this.isEmpty = true;

        this.notifyAll();

        return this.value;
    }

    public synchronized void put(Task value)
    {
        while (!this.isEmpty)
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException exception)
            {
                System.out.println(exception.getStackTrace());
            }
        }

        this.isEmpty = false;

        this.value = value;

        this.notifyAll();
    }
}
