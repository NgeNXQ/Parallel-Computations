package task3;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class MainFrame extends JFrame 
{
    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 500;

    private Canvas canvas;

    private JPanel panelButtons;

    private JButton buttonExit;
    private JButton buttonPriorityMax;
    private JButton buttonPriorityMin;

    private Container container;

    public MainFrame()
    {
        this.canvas = new Canvas();

        this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        this.container = this.getContentPane();
        this.container.add(this.canvas, BorderLayout.CENTER);

        this.panelButtons = new JPanel();
        this.panelButtons.setBackground(Color.lightGray);

        this.buttonExit = new JButton("Exit");
        this.buttonPriorityMax = new JButton("Max");
        this.buttonPriorityMin = new JButton("Min");

        this.buttonExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        this.buttonPriorityMax.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Ball ball = new Ball(MainFrame.this.canvas, Color.RED, 0, 0);
                MainFrame.this.canvas.add(ball);

                BallThread thread = new BallThread(ball);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();

                System.out.println("Thread name = " + thread.getName());
            }
        });

        this.buttonPriorityMin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Ball ball = new Ball(MainFrame.this.canvas, Color.BLUE, 0, 0);
                MainFrame.this.canvas.add(ball);

                BallThread thread = new BallThread(ball);
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();

                System.out.println("Thread name = " + thread.getName());
            }
        });

        this.panelButtons.add(this.buttonPriorityMin);
        this.panelButtons.add(this.buttonPriorityMax);
        this.panelButtons.add(this.buttonExit);

        this.container.add(this.panelButtons, BorderLayout.SOUTH);
    }
}