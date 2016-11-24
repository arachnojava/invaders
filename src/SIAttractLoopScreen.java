import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHRuntimeMetrics;
import mhframework.media.MHFont;
import mhframework.media.MHImageFont;
import mhframework.media.MHResourceManager;


public class SIAttractLoopScreen extends SIScreenBase
{
    SIScreenBase TITLE_SCREEN = new TitleAndCreditsScreen(this);
    SIScreenBase SCORE_TABLE_SCREEN = new ScoreTableScreen(this);
    SIScreenBase CONTROLS_SCREEN = new ControlsScreen(this);
    SIScreenBase FBI_SCREEN = new FBIMessageScreen(this);
    
    private SIScreenBase currentScreen;
    private boolean flashOn = true;
    private long lastFlashTime = MHGame.getGameTimerValue();
    private static final long FLASH_DELAY = MHRuntimeMetrics.secToNano(1)/2;
    
    public SIAttractLoopScreen()
    {
        changeScreen(TITLE_SCREEN);
    }
    
    
    public void render(Graphics2D g)
    {
        currentScreen.render(g);
    }
    
    
    public void advance()
    {
        currentScreen.advance();
        if (MHGame.getGameTimerValue() - lastFlashTime >= FLASH_DELAY)
        {
            lastFlashTime = MHGame.getGameTimerValue();
            flashOn = !flashOn;
        }
    }
    
    
    void changeScreen(SIScreenBase screen)
    {
        currentScreen = screen;
        currentScreen.load();
        currentScreen.setStartTime();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // Not used since all controls are key presses.
    }


    @Override
    public void load()
    {
        setFinished(false);
        changeScreen(TITLE_SCREEN);
        super.load();
    }


    @Override
    public void unload()
    {
    }

    
    
    

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_1)
        {
            SIDataModel.getInstance().setNumPlayers(1);
            setNextScreen(new SIGameScreen());
            setFinished(true);
        }
        else if (e.getKeyCode() == KeyEvent.VK_2)
        {
            SIDataModel.getInstance().setNumPlayers(2);
            setNextScreen(new SIGameScreen());
            setFinished(true);
        }
        else
            super.keyReleased(e);
    }


    void drawStartMessage(Graphics2D g)
    {
        if (flashOn)
            centerText(g, "PRESS (1) OR (2) TO BEGIN", MHDisplayModeChooser.getScreenSize().height-40, getFont());
    }
}


class TitleAndCreditsScreen extends SIScreenBase
{
    private SIAttractLoopScreen parentScreen;
    
    public TitleAndCreditsScreen(SIAttractLoopScreen parent)
    {
        parentScreen = parent;
        setDisplayDurationSeconds(5);
    }
    
    public void render(Graphics2D g)
    {
        int y = MHDisplayModeChooser.getScreenSize().height / 4;
        MHFont font = super.getFont();
        
        g.drawImage(getBackgroundImage(), 0, 0, null);
        
        centerText(g, "INVADERS, POSSIBLY FROM SPACE", y, font);
        y += 80;
        centerText(g, "BY", y, font);
        y += 30;
        centerText(g, "MICHAEL HENSON", y, font);
        y += 80;
        centerText(g, "SE456: ARCHITECTURE OF COMPUTER GAMES", y, font);
        y += 30;
        centerText(g, "DEPAUL UNIVERSITY", y, font);
        y += 30;
        centerText(g, "PROFESSOR ED KEENAN", y, font);
        y += 60;
        centerText(g, "OCTOBER 2010", y, font);
    }
    
    public void advance()
    {
        if (isDisplayTimeExpired())
            parentScreen.changeScreen(parentScreen.CONTROLS_SCREEN);
    }
    
}


class ScoreTableScreen extends SIScreenBase
{
    private SIAttractLoopScreen parentScreen;
    
    public ScoreTableScreen(SIAttractLoopScreen parent)
    {
        parentScreen = parent;
        setDisplayDurationSeconds(10);
    }
    
    public void render(Graphics2D g)
    {
        g.drawImage(getBackgroundImage(), 0, 0, null);

        int y = MHDisplayModeChooser.getScreenSize().height / 4;
        MHFont font = getFont();
        SIDataModel data = SIDataModel.getInstance();
        
        centerText(g, "SCORE ADVANCE TABLE", y, font);

        y += 100;
        
        // Show alien sprites.
        // UFO
        int x = MHDisplayModeChooser.getScreenSize().width/2 - data.getAlienSprite(3, 0).getWidth(null);
        Image sprite = data.getAlienSprite(3, 0);
        int w = sprite.getWidth(null);
        int h = sprite.getHeight(null);
        g.drawImage(sprite, x-w, y-h, w, h, null);
        font.drawString(g, " = ? MYSTERY", x, y);

        y += 60;

        // Aliens
        for (int alienType = 2; alienType >= 0; alienType--)
        {
            sprite = data.getAlienSprite(alienType, 1);
            w = sprite.getWidth(null);
            h = sprite.getHeight(null);
            g.drawImage(sprite, x-w, y-h, w, h, null);
            font.drawString(g, " = "+SIDataModel.getInstance().getScoreValue(alienType) + " POINTS", x, y);
            y += 60;
        }
        
        parentScreen.drawStartMessage(g);
        parentScreen.drawHUD(g);
    }
    
    public void advance()
    {
        if (isDisplayTimeExpired())
            parentScreen.changeScreen(parentScreen.FBI_SCREEN);
    }
}


class ControlsScreen extends SIScreenBase
{
    private SIAttractLoopScreen parentScreen;
    
    public ControlsScreen(SIAttractLoopScreen parent)
    {
        parentScreen = parent;
        setDisplayDurationSeconds(10);
    }
    
    public void render(Graphics2D g)
    {
        g.drawImage(getBackgroundImage(), 0, 0, null);
        
        int x1 = MHDisplayModeChooser.getScreenSize().width/4;
        int x2 = x1 + MHDisplayModeChooser.getScreenSize().width/2;
        int y = MHDisplayModeChooser.getScreenSize().height / 4;
        MHFont font = super.getFont();
        
        centerText(g, "CONTROLS", y, font);
        y += 80;
        centerText(g, "PLAYER 1", x1, y, font);
        centerText(g, "PLAYER 2", x2, y, font);
        y += 60;
        centerText(g, "MOVE LEFT: "+SIControls.P1_MOVE_LEFT.getKeyText(), x1, y, font);
        centerText(g, "MOVE LEFT: "+SIControls.P2_MOVE_LEFT.getKeyText(), x2, y, font);
        y += 45;
        centerText(g, "MOVE RIGHT: "+SIControls.P1_MOVE_RIGHT.getKeyText(), x1, y, font);
        centerText(g, "MOVE RIGHT: "+SIControls.P2_MOVE_RIGHT.getKeyText(), x2, y, font);
        y += 45;
        centerText(g, "FIRE: "+SIControls.P1_FIRE.getKeyText(), x1, y, font);
        centerText(g, "FIRE: "+SIControls.P2_FIRE.getKeyText(), x2, y, font);
     
        parentScreen.drawStartMessage(g);
        parentScreen.drawHUD(g);
    }
    
    public void advance()
    {
        if (isDisplayTimeExpired())
            parentScreen.changeScreen(parentScreen.SCORE_TABLE_SCREEN);
    }
}


class FBIMessageScreen extends SIScreenBase
{
    private SIAttractLoopScreen parentScreen;
    private Image image;
    private int x, y, w, h;
    
    public FBIMessageScreen(SIAttractLoopScreen parent)
    {
        parentScreen = parent;
        image = MHResourceManager.loadImage(SIDataModel.IMAGE_DIRECTORY+"WinnersDontUseDrugs.jpg");
        setDisplayDurationSeconds(10);
    }
    
    public void load()
    {
        x = 0;
        y = 0;
        w = MHDisplayModeChooser.getWidth();
        h = MHDisplayModeChooser.getHeight();
    }
    
    public void render(Graphics2D g)
    {
        g.drawImage(image, x, y, w, h, null);
    }
    
    public void advance()
    {
        if (isDisplayTimeExpired())
            parentScreen.changeScreen(parentScreen.TITLE_SCREEN);
    }
}
