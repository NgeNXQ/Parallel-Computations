package task2;

import javax.swing.JFrame;

final class Main
{
    public static void main(String[] args)
    {
        MainFrame mainFrame = new MainFrame();

        mainFrame.setTitle("Labwork #1 (1.2). IP-14 Babich Denys");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        System.out.println("Thread name = " + Thread.currentThread().getName());
    }
}