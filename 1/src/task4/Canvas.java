package task4;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import java.util.ArrayList;

public final class Canvas extends JPanel
{
    private ArrayList<Ball> balls;
    private ArrayList<Pocket> pockets;

    public Canvas()
    {
        this.balls = new ArrayList<Ball>();
        this.pockets = new ArrayList<Pocket>();
    }

    public void add(Ball ball)
    {
        this.balls.add(ball);
    }

    public void add(Pocket pocket)
    {
        this.pockets.add(pocket);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;

        for(int i = 0; i < this.balls.size(); ++i)
        {
            this.balls.get(i).draw(g2);
        }

        for(int i = 0; i < this.pockets.size(); ++i)
        {
            this.pockets.get(i).draw(g2);
        }
    }

    public boolean hasCollided(Ball ball)
    {
        for (Pocket pocket : this.pockets) 
        {
            if (ball.isInPocket(pocket.getX(), pocket.getY()))
            {
                this.balls.remove(ball);

                MainFrame.updateScore();

                return true;
            }
        }

        return false;
    }
}