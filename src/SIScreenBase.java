import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHRuntimeMetrics;
import mhframework.MHScreen;
import mhframework.media.MHFont;
import mhframework.media.MHImageFont;
import mhframework.media.MHStarField;


public abstract class SIScreenBase extends MHScreen
{
    private MHFont font;
    private static MHStarField backgroundImage;
    private SIHUD hud;
    private long startTime, displayDuration;

    public SIScreenBase()
    {
        hud = new SIHUD();
        // TODO:  Can we hide the mouse cursor?
    }
    
    
    protected Image getBackgroundImage()
    {
        if (backgroundImage == null)
            backgroundImage = new MHStarField(MHDisplayModeChooser.getScreenSize().width, MHDisplayModeChooser.getScreenSize().height, 200);
        
        return backgroundImage.getImage();
    }

    
    protected MHFont getFont()
    {
        if (font == null)
            font = new MHFont(MHImageFont.EngineFont.NES1);
        
        return font;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Not used since all controls are key presses.
    }


    @Override
    public void load()
    {
        setStartTime();
    }


    @Override
    public void unload()
    {
    }
    
    
    protected void drawHUD(Graphics2D g)
    {
        hud.render(g, getFont());
    }
    
    
    protected void setStartTime()
    {
        startTime = MHGame.getGameTimerValue();
    }
    
    
    protected void setDisplayDurationSeconds(int duration)
    {
        displayDuration = MHRuntimeMetrics.secToNano(duration);
    }
    
    
    protected boolean isDisplayTimeExpired()
    {
        return MHGame.getGameTimerValue() - startTime > displayDuration;
    }

    
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            MHGame.setProgramOver(true);
        
        super.keyReleased(e);
    }
    
    
}
