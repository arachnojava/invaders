import java.util.ArrayList;


public class SICollisionProcessor
{
    private SIAlienSwarm aliens;
    private SIMysteryShip ufo;
    private SIPlayerSprite[] players;
    private SIShield[] shields;
    private SIProjectileManager projectiles;

    public SICollisionProcessor(SIPlayerSprite[] players, SIAlienSwarm aliens, SIMysteryShip ufo, SIShield[] shields, SIProjectileManager projectiles)
    {
        this.players = players;
        this.aliens = aliens;
        this.ufo = ufo;
        this.shields = shields;
        this.projectiles = projectiles;
    }
    
    
    public void processCollisions()
    {
        // All projectiles to shields.
        processCollisions(projectiles, shields);
        
        // Player projectiles to aliens.
        processCollisions(projectiles, aliens);
        
        // Player projectiles to UFO.
        processCollisions(projectiles, ufo);
        
        // Alien projectiles to players.
        processCollisions(projectiles, players);
        
        // Player projectiles to alien projectiles.
        processCollisions(projectiles);
        
        // Aliens to shields.
        processCollisions(aliens, shields);
    }
    
    
    private void processCollisions(SIAlienSwarm aliens, SIShield[] shields)
    {
        for (int s = 0; s < shields.length; s++)
        {
            if (aliens.isColliding(shields[s]))
            {
                for (int r = 0; r < SIShield.BRICKS_HIGH; r++)
                    if (shields[s].getRowBounds(r).intersects(aliens.getBounds()))
                        shields[s].destroyRow(r);
            }
        }
    }


    private void processCollisions(SIProjectileManager pm)
    {
        ArrayList<SIProjectile> ap = pm.getProjectileList(aliens);
        ArrayList<SIProjectile> p1p = pm.getProjectileList(players[0]);
        ArrayList<SIProjectile> p2p = null;
        
        if (SIDataModel.getInstance().getNumPlayers() == 2)
            p2p = pm.getProjectileList(players[1]);
        
        for (SIProjectile bomb : ap)
        {
            for (SIProjectile bullet : p1p)
            {
                if (bomb.isAlive() && bullet.isAlive() && bomb.isColliding(bullet))
                {
                    bomb.setState(SISprite.State.DEAD);
                    bullet.setState(SISprite.State.DEAD);
                    continue;
                }
            }
            if (p2p != null && bomb.isAlive())
                for (SIProjectile bullet : p2p)
                {
                    if (bomb.isAlive() && bullet.isAlive() && bomb.isColliding(bullet))
                    {
                        bomb.setState(SISprite.State.DEAD);
                        bullet.setState(SISprite.State.DEAD);
                        continue;
                    }
                }
        }
    }


    private void processCollisions(SIProjectileManager pm,
            SIPlayerSprite[] players)
    {
        for (SIProjectile p : pm.getProjectileList(aliens))
            for (int player = 0; player < players.length; player++)
            {
                if (p.getBounds().intersects(players[player].getBounds()) && p.isAlive() && players[player].isAlive())
                {
                    p.setState(SISprite.State.DEAD);
                    players[player].destroy();
                }
            }
    }


    private void processCollisions(SIProjectileManager pm, SIMysteryShip ufo)
    {
        for (SIProjectile p : pm.getProjectileList())
        {
            if (p.getBounds().intersects(ufo.getBounds()) && p.isAlive() && ufo.isAlive())
            {
                ufo.destroy();
                p.setState(SISprite.State.DEAD);
                int player = ((SIPlayerSprite)p.getSource()).getPlayerNumber();
                int points = SIDataModel.getInstance().getScoreValue(ufo.getType());
                SIDataModel.getInstance().addPlayerScore(player, points);
                return;
            }
        }
    }


    private void processCollisions(SIProjectileManager pm, SIShield[] s)
    {
        for (SIProjectile p : pm.getProjectileList())
        {
            for (int i = 0; i < s.length; i++)
                if (s[i].getBounds().intersects(p.getBounds()) && p.isAlive())
                    s[i].collideWith(p);
        }
    }

    
    private void processCollisions(SIProjectileManager pm, SIAlienSwarm a)
    {
        for (int player = 0; player < players.length; player++)
            for (SIProjectile p : pm.getProjectileList(players[player]))
                if (a.getBounds().intersects(p.getBounds()) && p.isAlive())
                    a.collideWith(p);
    }

}
