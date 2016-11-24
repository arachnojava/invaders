import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHRuntimeMetrics;


public class SIAlienSwarm implements SICollidable
{
    SIAlien[][] swarm = new SIAlien[5][11];
    int frame = 0;
    int originY = 5 * SIDataModel.getInstance().getAlienSprite(0,0).getHeight(null);
    int x, y;
    long delay = MHRuntimeMetrics.secToNano(1),
         originalDelay = MHRuntimeMetrics.secToNano(1),
         lastMoveTime = MHGame.getGameTimerValue();
    int speed = 20;
    int dx = 20, 
        dy = SIDataModel.getInstance().getAlienSprite(0,0).getHeight(null)/2;
    private Rectangle2D bounds;
    int leftEdge = Math.abs(dx);
    private int rightEdge = (int) (MHDisplayModeChooser.getWidth() - Math.abs(dx));
    
    int alienCount = 0, totalAliens = 0;;
    SwarmState state;
    
    SwarmState MOVING_RIGHT = new MovingRight(this);
    SwarmState MOVING_LEFT = new MovingLeft(this);
    SwarmState DROPPING = new Dropping(this); 
    SwarmState PAUSED = new Paused(this);
    
    public SIAlienSwarm()
    {
        reset(0);
    }
    
    
    public void render(Graphics2D g)
    {
        for (int r = 0; r < swarm.length; r++)
            for (int c = 0; c < swarm[r].length; c++)
                swarm[r][c].render(g, frame);

        if (SIDataModel.DEBUG_MODE)
        {
            bounds = calculateBounds();
            g.setColor(Color.RED);
            g.drawRect((int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        }
    }
    
    
    public void advance()
    {
        if (MHGame.getGameTimerValue() - lastMoveTime >= delay)
        {
            frame = (frame+1) % 2;
            
            state.advance();
            updatePositions();
            bounds = calculateBounds();

//            if ((double)alienCount / totalAliens <= 0.8)
//            {
//                delay = (long)(delay * 0.85);
//                totalAliens = (int)(totalAliens * 0.8);
//            }
            
            lastMoveTime = MHGame.getGameTimerValue();
        }
    }
    
    
    void fire()
    {
        SIAlien shooter = pickShooter();
        
        if (shooter != null)
            SIProjectileManager.getInstance().alienFire(shooter, this);
    }
    
    private SIAlien pickShooter()
    {
        Random random = new Random();
        int attempts = 0;

        do
        {
            // Pick a column at random.
            int c = random.nextInt(swarm[0].length);
        
            // If column has active aliens, pick the bottom one.
            for (int r = swarm.length-1; r >= 0; r--)
                if (swarm[r][c].isAlive())
                    return swarm[r][c];
            
            attempts++;
        } while (attempts < 10);
        
        return null;
    }
    
    
    boolean isAtEdge()
    {
        if (bounds.getMinX() - dx < leftEdge && state == MOVING_LEFT)
            return true;
        else if (bounds.getMaxX() + dx > rightEdge && state == MOVING_RIGHT)
            return true;
        
        return false;
    }
    
    
    
    private void updatePositions()
    {
        int w = swarm[0][0].getWidth()+10;
        int h = swarm[0][0].getHeight()+10;
        
        for (int r = 0; r < swarm.length; r++)
            for (int c = 0; c < swarm[r].length; c++)
            {
                swarm[r][c].x = x+c*w;
                swarm[r][c].y = y+r*h;
            }
    }
    
    
    boolean checkForGroundHit()
    {
        if (y + bounds.getHeight() >= SIDataModel.getInstance().getGroundElevation())
        {
            y = (int) (SIDataModel.getInstance().getGroundElevation() - bounds.getHeight());
            return true;
        }
        
        return false;
    }
    
    
    public Rectangle2D calculateBounds()
    {
        int left = 9999;
        int top = 9999;
        int right = 0;
        int bottom = 0;

        for (int r = 0; r < swarm.length; r++)
            for (int c = 0; c < swarm[r].length; c++)
                if (swarm[r][c].isAlive())
                {
                    Rectangle2D spriteBounds = swarm[r][c].getBounds();
                    left = (int) Math.min(left, spriteBounds.getMinX());
                    top = (int) Math.min(top, spriteBounds.getMinY());
                    right = (int) Math.max(right, spriteBounds.getMaxX());
                    bottom = (int) Math.max(bottom, spriteBounds.getMaxY());
                }
        bounds = new Rectangle2D.Double(left, top, right-left, bottom-top);
        return bounds;
    }
    
    
    public Rectangle2D getBounds()
    {
        if (bounds == null)
            bounds = calculateBounds();
        
        return bounds;
    }
    
    public void reset(int wave)
    {
        // Lower starting position based on wave.
        y = originY + SIDataModel.getInstance().getAlienSprite(0,0).getHeight(null) * wave;
        x = SIDataModel.getInstance().getAlienSprite(0,0).getWidth(null) * 3;
        
        delay = originalDelay;
        
        state = MOVING_RIGHT;
        
        totalAliens = 0;
        for (int c = 0; c < swarm[0].length; c++)
        {
            swarm[0][c] = new SIAlien(2);
            swarm[1][c] = new SIAlien(1);
            swarm[2][c] = new SIAlien(1);
            swarm[3][c] = new SIAlien(0);
            swarm[4][c] = new SIAlien(0);
            totalAliens += 5;
        }
        
        alienCount = totalAliens;
        updatePositions();
        calculateBounds();
    }

    @Override
    public boolean isColliding(SISprite other)
    {
        return getBounds().intersects(other.getBounds());
    }


    public void collideWith(SIProjectile p)
    {
        for (int r = 0; r < swarm.length; r++)
            for (int c = 0; c < swarm[r].length; c++)
            if (swarm[r][c].isAlive() && swarm[r][c].getBounds().intersects(p.getBounds()) && p.isAlive())
            {
                swarm[r][c].destroy();
                alienCount--;
                delay = (long)(delay * 0.96);
                p.setState(SISprite.State.DEAD);
                int player = ((SIPlayerSprite)p.getSource()).getPlayerNumber();
                int points = SIDataModel.getInstance().getScoreValue(swarm[r][c].getType());
                SIDataModel.getInstance().addPlayerScore(player, points);
                return;
            }
    }
}


interface SwarmState
{
    public abstract void advance();
}


class MovingLeft implements SwarmState
{
    SIAlienSwarm a;

    public MovingLeft(SIAlienSwarm alienSwarm)
    {
        a = alienSwarm;
    }

    @Override
    public void advance()
    {
        a.x -= a.dx;
        if (a.isAtEdge())
            a.state = a.DROPPING;
        else
            a.fire();
    }
    
}


class MovingRight implements SwarmState
{
    SIAlienSwarm a;

    public MovingRight(SIAlienSwarm alienSwarm)
    {
        a = alienSwarm;
    }

    @Override
    public void advance()
    {
        a.x += a.dx;
        if (a.isAtEdge())
            a.state = a.DROPPING;
        else
            a.fire();
    }
    
}


class Dropping implements SwarmState
{
    SIAlienSwarm a;

    public Dropping(SIAlienSwarm alienSwarm)
    {
        a = alienSwarm;
    }

    @Override
    public void advance()
    {
        a.y += a.dy;
        if (a.checkForGroundHit())
            a.state = a.PAUSED;
        else
        {
          if (a.x <= a.leftEdge)
              a.state = a.MOVING_RIGHT;
          else
              a.state = a.MOVING_LEFT;
        }
    }
}


class Paused implements SwarmState
{
    SIAlienSwarm a;

    public Paused(SIAlienSwarm alienSwarm)
    {
        a = alienSwarm;
    }

    @Override
    public void advance()
    {
    }
}

