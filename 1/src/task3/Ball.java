package task3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public final class Ball
{
    private final int SIZE_X = 20;
    private final int SIZE_Y = 20;

    private Component сomponent;

    private Color color;

    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;

    public Ball(Component component, Color color, int positionX, int positionY)
    {
        this.color = color;
        this.сomponent = component;

        this.x = positionX;
        this.y = positionY;
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(this.x, this.y, this.SIZE_X, this.SIZE_Y));
    }

    public void move()
    {
        this.x += this.dx;
        this.y += this.dy;

        if(this.x < 0)
        {
            this.x = 0;
            this.dx = -this.dx;
        }

        if(x + this.SIZE_X >= this.сomponent.getWidth())
        {
            this.dx = -this.dx;
            this.x = this.сomponent.getWidth() - this.SIZE_X;
        }

        if(this.y < 0)
        {
            this.y = 0;
            this.dy = -this.dy;
        }

        if(this.y + this.SIZE_Y >= this.сomponent.getHeight())
        {
            this.dy = -this.dy;
            this.y = this.сomponent.getHeight() - this.SIZE_Y;
        }

        this.сomponent.repaint();
    }
}