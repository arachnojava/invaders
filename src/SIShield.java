import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.Random;
import mhframework.MHDisplayModeChooser;
import mhframework.media.MHResourceManager;


public class SIShield extends SISprite
{
    static final int BRICKS_HIGH = 4;
    static final int BRICKS_WIDE = 5;

    private ShieldBrick[][] array;

    
    public SIShield(int x)
    {
        reset();
        this.x = x - getWidth()/2;
        y = 480;
    }

    
    public void render(Graphics2D g)
    {
        int w = array[0][1].getWidth();
        int h = array[0][1].getHeight();
        
        for (int r = 0; r < array.length; r++)
            for (int c = 0; c < array[r].length; c++)
                array[r][c].render(g, x+c*w, y+r*h);
        
        for (int r = 0; r < array.length; r++)
            for (int c = 0; c < array[r].length; c++)
                if (array[r][c].getState() == SISprite.State.DEAD)
                    g.drawImage(array[r][c].damageEffect, x+c*w-3, y+r*h-3, null);
        
        if (SIDataModel.DEBUG_MODE)
            drawBounds(g);
    }
    

    private ShieldBrick createBrick(int brickType)
    {
        if (brickType == -1)
            return new NullBrick(brickType);
        
        return new ShieldBrick(brickType);
    }
    
    

    @Override
    public int getWidth()
    {
        int maxX = 0;
        for (int r = 0; r < array.length; r++)
            for (int c = 0; c < array[r].length; c++)
                if (array[r][c].getState() == State.ALIVE)
                    maxX = Math.max(maxX, array[r][c].x + array[r][c].getWidth());
        
        return maxX - x;
    }


    @Override
    public int getHeight()
    {
        int maxY = 0;
        for (int r = 0; r < array.length; r++)
            for (int c = 0; c < array[r].length; c++)
                if (array[r][c].getState() == State.ALIVE)
                    maxY = Math.max(maxY, array[r][c].y + array[r][c].getHeight());
        
        return maxY - y;
    }


    public void collideWith(SIProjectile p)
    {
        for (int r = 0; r < array.length; r++)
            for (int c = 0; c < array[r].length; c++)
                if (array[r][c].isColliding(p))
                {
                    array[r][c].destroy();
                    p.setState(State.DEAD);
                    return;
                }
    }


    public Rectangle2D getRowBounds(int row)
    {
        double left = x;
        double top = y + row * array[row][0].getHeight();
        double height = array[row][0].getHeight();
        double width = array[row][0].getWidth() * array[row].length;
        
        return new Rectangle2D.Double(left, top, width, height);
    }

    
    public void destroyRow(int row)
    {
        for (int c = 0; c < array[row].length; c++)
            array[row][c].destroy();
    }


    public void reset()
    {
        array = new ShieldBrick[][] 
                                  {
                                          {createBrick(0), createBrick(1), createBrick(1), createBrick(1), createBrick(2)},
                                          {createBrick(1), createBrick(1), createBrick(1), createBrick(1), createBrick(1)},
                                          {createBrick(1), createBrick(1), createBrick(1), createBrick(1), createBrick(1)},
                                          {createBrick(1), createBrick(3), createBrick(-1), createBrick(4), createBrick(1)}
                                  };
    }
    
}


class ShieldBrick extends SISprite
{
    private static Image[] images;
    public Image damageEffect;
    private int type;
    
    public ShieldBrick(int brickType)
    {
        if (images == null)
        {
            images = new Image[5];
            for (int i = 0; i < images.length; i++)
                images[i] = MHResourceManager.loadImage(SIDataModel.IMAGE_DIRECTORY + "Shield"+i+".gif");
        }
        type = brickType;
        setState(State.ALIVE);
    }

    
    public boolean isColliding(SISprite other)
    {
        if (getState() == State.DEAD)
            return false;
        
        return getBounds().intersects(other.getBounds());
    }
    

    public void destroy()
    {
        Random rand = new Random();
        if (damageEffect == null)
            damageEffect = new BufferedImage(getWidth()+10, getHeight()+10, BufferedImage.TYPE_INT_ARGB);
        Graphics g = damageEffect.getGraphics();
        g.setColor(SIGameScreen.BG_COLOR);
        for (int i = 0; i < 20; i++)
            g.fillOval(-3+rand.nextInt(getWidth()+5), -3+rand.nextInt(getHeight()+5), 5, 5);

        if (getState() != State.DEAD)
        {
            setState(State.DEAD);
            SIAudio.play(SIAudio.SHIELD_HIT);
        }
    }

    
    public void render(Graphics2D g, int x, int y)
    {
        this.x = x;
        this.y = y;
        
        g.drawImage(images[type], x, y, null);
        
        //if (getState() == State.DEAD)
        //    g.drawImage(damageEffect, x, y, null);
    }
    
    
    @Override
    public int getWidth()
    {
        return images[type].getWidth(null);
    }

    
    @Override
    public int getHeight()
    {
        return images[type].getHeight(null);
    }
}

class NullBrick extends ShieldBrick
{
    public NullBrick(int brickType)
    {
        super(1);
        setState(State.DEAD);
    }
    
    
    public void render(Graphics2D g, int x, int y)
    {
    }
}