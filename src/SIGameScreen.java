import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHRuntimeMetrics;


public class SIGameScreen extends SIScreenBase
{
    public static final Color BG_COLOR = Color.BLACK;
    SICollisionProcessor collisions;
    SIAlienSwarm aliens = new SIAlienSwarm();
    SIMysteryShip ufo = new SIMysteryShip();
    SIPlayerSprite[] player;
    SIShield[] shields;
    SIProjectileManager projectiles;
    int nPlayers;
    private long readyStartTime, gameOverTime, flashTime;
    private boolean flash = false;
    
    
    
    private enum State
    {
        READY, PLAYING, GAME_OVER;
    }
    
    State state = State.READY;
    
    public SIGameScreen()
    {
        nPlayers = SIDataModel.getInstance().getNumPlayers();
        player = new SIPlayerSprite[nPlayers];
        for (int i = 0; i < nPlayers; i++)
            player[i] = new SIPlayerSprite(i+1);
        
        shields = new SIShield[3];
        int w = MHDisplayModeChooser.getWidth();
        shields[0] = new SIShield((int)(w*0.25));
        shields[1] = new SIShield(w/2);
        shields[2] = new SIShield((int)(w*0.75));
        
        projectiles = SIProjectileManager.getInstance();
        
        collisions = new SICollisionProcessor(player, aliens, ufo, shields, projectiles);
    }
    
    
    public void load()
    {
        reset(0);
    }
    
    public void advance()
    {
        if (state == State.GAME_OVER)
        {
            if (MHGame.getGameTimerValue() - gameOverTime > MHRuntimeMetrics.secToNano(5))
                setFinished(true);
            return;
        }
        
        if (state == State.READY)
        {
            if (MHGame.getGameTimerValue() - readyStartTime > MHRuntimeMetrics.secToNano(3))
                state = State.PLAYING;
        }

        for (int i = 0; i < nPlayers; i++)
            player[i].advance();
        projectiles.advance();

        if (state == State.PLAYING)
        {
            // Check for game over.
            if (isGameOver())
            {
                // Destroy players.
                for (int i = 0; i < nPlayers; i++)
                    player[i].destroy();
            
                state = State.GAME_OVER;
                gameOverTime = MHGame.getGameTimerValue();
            }
            else if (isWaveComplete())
            {
                SIDataModel.getInstance().setWaveNumber(SIDataModel.getInstance().getWaveNumber()+1);
                reset(SIDataModel.getInstance().getWaveNumber());
            }
            else
            {
                aliens.advance();
                ufo.advance();
                collisions.processCollisions();
            }
        }
    }
    
    
    public boolean isGameOver()
    {
        // Check for aliens on ground.
        if (aliens.checkForGroundHit())
            return true;
        
        // Check for depleted lives.
        boolean livesGone = true;
        for (int i = 0; i < nPlayers; i++)
            if (SIDataModel.getInstance().getLives(i+1) > 0)
                livesGone = false;
        
        return livesGone;
    }
    
    
    public boolean isWaveComplete()
    {
        return aliens.alienCount <= 0;
    }
    
    
    public void reset(int wave)
    {
        for (int s = 0; s < shields.length; s++)
            shields[s].reset();
        
        aliens.reset(wave);
        projectiles.reset();
        state = State.READY;
        SIAudio.play(SIAudio.READY);
        readyStartTime = MHGame.getGameTimerValue();
    }
    
    
    public void render(Graphics2D g)
    {
        super.fill(g, BG_COLOR);
        
        for (int i = 0; i < nPlayers; i++)
            player[i].render(g);

        for (int i = 0; i < shields.length; i++)
            shields[i].render(g);
        
        if (state == State.READY)
            centerText(g, "READY!", 300, getFont());
        else
            aliens.render(g);
        
        ufo.render(g, 0);
        
        projectiles.render(g);
        
        if (state == State.GAME_OVER)
        {
            if (MHGame.getGameTimerValue() - flashTime > MHRuntimeMetrics.secToNano(1)/2)
            {
                flash = !flash;
                flashTime = MHGame.getGameTimerValue();
            }
            if (flash)
                centerText(g, "GAME OVER", 100, getFont());

        }

        drawHUD(g);
        
    }


    @Override
    public void keyReleased(KeyEvent e)
    {
        if (SIControls.P1_MOVE_RIGHT.equals(e))
            player[0].movingRight = false;
        if (SIControls.P1_MOVE_LEFT.equals(e))
            player[0].movingLeft = false;
        if (SIControls.P1_FIRE.equals(e))
            player[0].firing = false;

        if (nPlayers == 2)
        {
            if (SIControls.P2_MOVE_RIGHT.equals(e))
                player[1].movingRight = false;
            if (SIControls.P2_MOVE_LEFT.equals(e))
                player[1].movingLeft = false;
            if (SIControls.P2_FIRE.equals(e))
                player[1].firing = false;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_F5)
            SIDataModel.DEBUG_MODE = !SIDataModel.DEBUG_MODE;
        if (e.getKeyCode() == KeyEvent.VK_F9)
            SIProjectileManager.getInstance().setPlayerProjectileLimit(10);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            setFinished(true);
            return;
        }
        
        super.keyReleased(e);
    }


    @Override
    public void keyPressed(KeyEvent e)
    {
        if (SIControls.P1_MOVE_RIGHT.equals(e))
            player[0].movingRight = true;
        if (SIControls.P1_MOVE_LEFT.equals(e))
            player[0].movingLeft = true;
        if (SIControls.P1_FIRE.equals(e))
            player[0].firing = true;

        if (nPlayers == 2)
        {
            if (SIControls.P2_MOVE_RIGHT.equals(e))
                player[1].movingRight = true;
            if (SIControls.P2_MOVE_LEFT.equals(e))
                player[1].movingLeft = true;
            if (SIControls.P2_FIRE.equals(e))
                player[1].firing = true;
        }
        super.keyPressed(e);
    }
    
    
}
