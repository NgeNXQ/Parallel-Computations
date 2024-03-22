package task3;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import java.util.ArrayList;

public final class Canvas extends JPanel
{
    private ArrayList<Ball> balls;

    public Canvas()
    {
        this.balls = new ArrayList<Ball>();
    }

    public void add(Ball b)
    {
        this.balls.add(b);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D)graphics;

        for(int i = 0; i < balls.size(); ++i)
        {
            this.balls.get(i).draw(g2);
        }
    }
}