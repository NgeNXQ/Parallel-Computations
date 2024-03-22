package task2;

import javax.swing.JFrame;
import javax.swing.JLabel;
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

    private static int score = 0;

    private static JPanel panelScore;

    private static JLabel labelScore;

    private Canvas canvas;
    private Container container;

    private JPanel panelButtons;

    private JButton buttonStop;
    private JButton buttonStart;
    private JButton buttonSpawn;

    public MainFrame()
    {
        this.canvas = new Canvas();

        this.container = this.getContentPane();
        this.container.add(this.canvas, BorderLayout.CENTER);

        this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);

        //System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        this.panelButtons = new JPanel();
        this.panelButtons.setBackground(Color.lightGray);

        MainFrame.panelScore = new JPanel();
        MainFrame.panelScore.setBackground(Color.lightGray);

        MainFrame.labelScore = new JLabel();

        this.buttonStop = new JButton("Exit");
        this.buttonStart = new JButton("Ball");
        this.buttonSpawn = new JButton("Pocket");

        this.buttonStart.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Ball ball = new Ball(MainFrame.this.canvas);
                MainFrame.this.canvas.add(ball);

                BallThread thread = new BallThread(ball, MainFrame.this.canvas);
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

        this.buttonSpawn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Pocket pocket = new Pocket(MainFrame.this.canvas);
                MainFrame.this.canvas.add(pocket);
            }
        });

        this.panelButtons.add(this.buttonStart);
        this.panelButtons.add(this.buttonSpawn);
        this.panelButtons.add(this.buttonStop);

        MainFrame.panelScore.add(MainFrame.labelScore);

        this.container.add(this.panelButtons, BorderLayout.SOUTH);
        this.container.add(MainFrame.panelScore, BorderLayout.NORTH);
    }

    public static synchronized void updateScore()
    {
        MainFrame.labelScore.setText("Score: " + (++MainFrame.score));
    }
}