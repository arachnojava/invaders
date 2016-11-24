import java.awt.Graphics2D;
import java.awt.Image;
import mhframework.MHDisplayModeChooser;


public class SIProjectile extends SISprite
{
    private Image image;
    private int speed;
    private SISprite source;

    public SIProjectile()
    {
        setState(State.DEAD);
    }
    
    public void fire(Image i, SIPlayerSprite shooter, int velocity)
    {
        image = i;
        source = shooter;
        x = shooter.x + shooter.getWidth()/2 - getWidth()/2;
        y = shooter.y - getHeight()/2;
        speed = velocity;
        setState(State.ALIVE);
    }

    
    public void fire(Image i, SIAlien shooter, int velocity)
    {
        image = i;
        source = shooter;
        x = shooter.x + shooter.getWidth()/2 - getWidth()/2;
        y = shooter.y + shooter.getHeight();
        speed = velocity;
        setState(State.ALIVE);
    }

    
    public SISprite getSource()
    {
        return source;
    }
    
    public void advance()
    {
        y += speed;
        
        // If off screen, projectile is dead.
        if (y < 0-getHeight() || y > SIDataModel.getInstance().getGroundElevation())
            setState(State.DEAD);
    }
    

    @Override
    public int getHeight()
    {
        return image.getHeight(null);
    }

    @Override
    public int getWidth()
    {
        return image.getWidth(null);
    }


    public void render(Graphics2D g)
    {
        g.drawImage(image, x, y, null);
        
        if (SIDataModel.DEBUG_MODE)
            drawBounds(g);
    }

    public boolean isAlive()
    {
        return getState() == State.ALIVE;
    }

}
