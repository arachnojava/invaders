import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHRuntimeMetrics;


public class SIPlayerSprite extends SISprite
{
    private static final Font font = new Font("SansSerif", Font.BOLD, 12);
    
    static int minX, maxX;
    boolean movingLeft = false;
    boolean movingRight = false;
    boolean firing = false;
    private int moveSpeed = 5;
    private Image image;
    private int playerNumber;
    private long firingDelay = MHRuntimeMetrics.secToNano(1)/4L;
    private long lastFireTime = MHGame.getGameTimerValue();
    private long destroyTime;
    private boolean flash = true;
    
    public SIPlayerSprite(int playerNum)
    {
        playerNumber = playerNum;
        image = SIDataModel.getInstance().getPlayerSprite();
        y = SIDataModel.getInstance().getGroundElevation() - image.getHeight(null);
        minX = 0;
        maxX = minX + MHDisplayModeChooser.getWidth() - image.getWidth(null);
        x = (maxX - minX) / 2;
        setState(State.ALIVE);
    }
    
    
    public void advance()
    {
        if (getState() == State.DEAD) return;
        
        if (getState() == State.DYING)
        {
            if (SIDataModel.getInstance().getLives(playerNumber) > 0)
            {
                if (MHGame.getGameTimerValue() - destroyTime > MHRuntimeMetrics.secToNano(2))
                    setState(State.ALIVE);
            }
            else
                setState(State.DEAD);
        }
        else
        {
        if (movingLeft)
        {
            x -= moveSpeed;
            if (x < minX)
                x = minX;
        }
        if (movingRight)
        {
            x += moveSpeed;
            if (x > maxX)
                x = maxX;
        }
        if (firing) fireLaser();
        }
    }
    
    
    @Override
    public int getWidth()
    {
        return image.getWidth(null);
    }


    @Override
    public int getHeight()
    {
        return image.getHeight(null);
    }

    
    public void fireLaser()
    {
        if (MHGame.getGameTimerValue() - lastFireTime > firingDelay)
        {
            SIProjectileManager.getInstance().playerFire(this);
            lastFireTime = MHGame.getGameTimerValue();
        }
    }


    public void render(Graphics2D g)
    {
        if (getState() == State.DEAD) return;
        
        if (getState() == State.DYING)
        {
            if (flash)
                g.drawImage(SIDataModel.getInstance().getBurstImage(), x, y, null);
            
            flash = !flash;
        }
        else
            g.drawImage(image, x, y, null);
        
        g.setFont(font);
        g.setColor(SIGameScreen.BG_COLOR);
        g.drawString(""+playerNumber, x+image.getWidth(null)/2-3, y+image.getHeight(null)-1);
        
        if (SIDataModel.DEBUG_MODE)
            drawBounds(g);
    }


    public int getPlayerNumber()
    {
        return playerNumber;
    }


    public void destroy()
    {
        if (isAlive())
        {
            setState(State.DYING);
            SIDataModel.getInstance().subtractLife(playerNumber);
            SIAudio.play(SIAudio.PLAYER_HIT);
            destroyTime = MHGame.getGameTimerValue();
            // TODO:  Particles?
        }
    }


    public boolean isAlive()
    {
        return getState() == State.ALIVE;
    }
}
