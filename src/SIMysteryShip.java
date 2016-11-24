import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHRuntimeMetrics;


public class SIMysteryShip extends SIAlien
{
    private int speed = 5;
    private boolean goRight = true;
    private boolean paused = true;
    private long delayTime, lastTime;
    
    
    public SIMysteryShip()
    {
        super(3);
        y = 70;
        reset();
    }
    
    
    public void advance()
    {
        if (!isAlive())
        {
            reset();
            return;
        }
        
        if (paused)
        {
            if (MHGame.getGameTimerValue() - lastTime > delayTime)
            {
                paused = false;
                SIAudio.play(SIAudio.UFO_FLY_BY);
            }
        }
        else if (goRight)
        {
            x += speed;
            if (x > MHDisplayModeChooser.getWidth())
            {
                goRight = false;
                reset();
            }
        }
        else
        {
            x -= speed;
            if (x < getWidth())
            {
                goRight = true;
                reset();
            }
        }
    }
    
    
    private void reset()
    {
        paused = true;
        setState(State.ALIVE);
        chooseSide();
        delayTime = calcDelayTime();
        lastTime = MHGame.getGameTimerValue();
    }
    
    
    private void chooseSide()
    {
        int r = (int)(Math.random() * 20);
        
        if (r % 2 == 0)
        {
            x = -getWidth();
            goRight = true;
        }
        else
        {
            x = MHDisplayModeChooser.getWidth();
            goRight = false;
        }
    }
    
    
    private long calcDelayTime()
    {
        return MHRuntimeMetrics.secToNano((int)(10 + Math.random() * 30));
    }


    @Override
    public void destroy()
    {
        if (isAlive())
        {
            setState(State.DYING);
            SIAudio.play(SIAudio.UFO_HIT);
            SIAudio.stop(SIAudio.UFO_FLY_BY);
        }
        // TODO:  Particles?
    }
}
