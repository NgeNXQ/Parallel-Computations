package task4;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import java.util.Random;

public final class Pocket 
{
    private final int SIZE_X = 40;
    private final int SIZE_Y = 40;
    public static final int RADIUS = 20;

    private Component component;

    private int x;
    private int y;

    public Pocket(Component component) 
    {
        this.component = component;

        Random random = new Random();

        x = random.nextInt(this.component.getWidth() - this.SIZE_X);
        y = random.nextInt(this.component.getHeight() - this.SIZE_Y);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(x, y, SIZE_X, SIZE_Y));
    }
}