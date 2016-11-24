import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;


public class SIProjectileManager
{
    private static SIProjectileManager instance;
    
    private int alienProjectileLimit = 1;
    private int playerProjectileLimit = 1;
    private Image playerProjectileImage, alienProjectileImage;
    private ArrayList<SIProjectile> projectiles;
    
    
    private SIProjectileManager()
    {
        projectiles = new ArrayList<SIProjectile>();
        playerProjectileImage = SIDataModel.getInstance().getPlayerProjectile();
        alienProjectileImage = SIDataModel.getInstance().getAlienProjectile();
    }
    
    
    public static SIProjectileManager getInstance()
    {
        if (instance == null)
            instance = new SIProjectileManager();
        
        return instance;
    }
    
    
    public void advance()
    {
        for (SIProjectile p : projectiles)
        {
            if (p.getState() == SISprite.State.ALIVE)
                p.advance();
        }
    }
    
    
    public void render(Graphics2D g)
    {
        for (SIProjectile p : projectiles)
        {
            if (p.getState() == SISprite.State.ALIVE)
                p.render(g);
        }
    }

    
    public void playerFire(SIPlayerSprite shooter)
    {
        // Count number of active projectiles for shooter.
        // If under limit, grab an unused one and initialize it.
        if (countProjectiles(shooter) < playerProjectileLimit)
        {
            getUnusedProjectile().fire(playerProjectileImage, shooter, -10);
            SIAudio.play(SIAudio.PLAYER_FIRE);
        }
    }

    
    private SIProjectile getUnusedProjectile()
    {
        SIProjectile foundOne = null;
        
        for (SIProjectile p : projectiles)
        {
            if (!p.isAlive())
                foundOne = p;
        }
        
        if (foundOne == null)
        {
            foundOne = new SIProjectile();
            projectiles.add(foundOne);
        }
        
        return foundOne;
    }
    

    public void alienFire(SIAlien shooter, SIAlienSwarm swarm)
    {
        // Count number of active alien projectiles.
        // If under limit, grab an unused one and initialize it.
        if (countProjectiles(swarm) < alienProjectileLimit)
        {
            getUnusedProjectile().fire(alienProjectileImage, shooter, 5);
            switch (shooter.getType())
            {
                case 0:
                    SIAudio.play(SIAudio.ALIEN_FIRE_1);
                    break;
                case 1:
                    SIAudio.play(SIAudio.ALIEN_FIRE_2);
                    break;
                case 2:
                    SIAudio.play(SIAudio.ALIEN_FIRE_3);
                    break;
            }
        }
    }
    
    
    private int countProjectiles(SIPlayerSprite shooter)
    {
        int count = 0;
        
        for (SIProjectile p : projectiles)
        {
            if (p.getSource() == shooter && p.isAlive())
                count++;
        }

        return count;
    }
    

    private int countProjectiles(SIAlienSwarm aliens)
    {
        return getProjectileList(aliens).size();
    }
    
    

    public void setAlienProjectileLimit(int limit)
    {
        alienProjectileLimit = limit;
    }

    
    public void setPlayerProjectileLimit(int limit)
    {
        playerProjectileLimit = limit;
    }


    public ArrayList<SIProjectile> getProjectileList()
    {
        return projectiles;
    }

    
    public ArrayList<SIProjectile> getProjectileList(SISprite shooter)
    {
        ArrayList<SIProjectile> filtered = new ArrayList<SIProjectile>();
        
        for (SIProjectile p : projectiles)
        {
            if (p.getSource() == shooter && p.isAlive())
                filtered.add(p);
        }
        
        return filtered;
    }


    public ArrayList<SIProjectile> getProjectileList(SIAlienSwarm aliens)
    {
        ArrayList<SIProjectile> filtered = new ArrayList<SIProjectile>();
        for (SIProjectile p : projectiles)
        {
            if (p.getSource() instanceof SIAlien && p.isAlive())
                filtered.add(p);
        }
        
        return filtered;
    }


    public void reset()
    {
        projectiles.clear();
    }
}
