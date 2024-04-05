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
    private JButton buttonSpawn;

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
        this.buttonSpawn = new JButton("Spawn");

        this.buttonExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        this.buttonSpawn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                final int MIN_BALLS_COUNT = 500;

                for (int i = 0; i < MIN_BALLS_COUNT; ++i)
                {
                    Ball ball = new Ball(MainFrame.this.canvas, Color.BLUE, 0, 0);
                    MainFrame.this.canvas.add(ball);
    
                    BallThread thread = new BallThread(ball);
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.start();
                }

                Ball ball = new Ball(MainFrame.this.canvas, Color.RED, 0, 0);
                MainFrame.this.canvas.add(ball);

                BallThread thread = new BallThread(ball);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            }
        });

        this.panelButtons.add(this.buttonExit);
        this.panelButtons.add(this.buttonSpawn);

        this.container.add(this.panelButtons, BorderLayout.SOUTH);
    }
}