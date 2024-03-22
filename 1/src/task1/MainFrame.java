package task1;

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

    private BallCanvas ballCanvas;

    private JPanel panelButtons;

    private JButton buttonStart;
    private JButton buttonStop;

    private Container container;

    public MainFrame()
    {
        this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.ballCanvas = new BallCanvas();

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        this.container = this.getContentPane();
        this.container.add(this.ballCanvas, BorderLayout.CENTER);

        this.panelButtons = new JPanel();
        this.panelButtons.setBackground(Color.lightGray);

        this.buttonStart = new JButton("Start");
        this.buttonStop = new JButton("Stop");

        this.buttonStart.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Ball ball = new Ball(MainFrame.this.ballCanvas);
                MainFrame.this.ballCanvas.add(ball);

                BallThread thread = new BallThread(ball);
                thread.start();

                System.out.println("Thread name = " + thread.getName());
            }
        });

        this.buttonStop.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        this.panelButtons.add(this.buttonStart);
        this.panelButtons.add(this.buttonStop);

        this.container.add(this.panelButtons, BorderLayout.SOUTH);
    }
}