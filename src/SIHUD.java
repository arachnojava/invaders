import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import mhframework.MHDisplayModeChooser;
import mhframework.media.MHFont;
import mhframework.media.MHImageFont;


public class SIHUD
{
    // Score board
    private ScoreBoard scoreBoard;
    
    // Lives display
    private LivesDisplay livesDisplay;
    
    public SIHUD()
    {
        scoreBoard = new ScoreBoard();
        livesDisplay = new LivesDisplay();
    }
    
    
    public void render(Graphics2D g, MHFont mhFont)
    {
        scoreBoard.render(g, mhFont);
        livesDisplay.render(g, mhFont);
    }
}


class ScoreBoard
{
    private int spacing = 30;

    
    public ScoreBoard()
    {
    }
    

    public void render(Graphics2D g, MHFont mhFont)
    {
        int p1x = 25; 
        int p2x = MHDisplayModeChooser.getScreenSize().width - mhFont.stringWidth("SCORE(2)") - 25;
        int y0 = 30;
        
        SIDataModel data = SIDataModel.getInstance();
        
        mhFont.drawString(g, "SCORE(1)", p1x, y0);
        String score = format(data.getPlayerScore(1));
        mhFont.drawString(g, score, p1x, y0+spacing);
        
        mhFont.drawString(g, "SCORE(2)", p2x, y0);
        score = format(data.getPlayerScore(2));
        p2x = MHDisplayModeChooser.getScreenSize().width - mhFont.stringWidth(score) - 25;
        mhFont.drawString(g, score, p2x, y0+spacing);
        
        int width = mhFont.stringWidth("HI-SCORE");
        int centerX = (MHDisplayModeChooser.getScreenSize().width/2 - (width/2));
        mhFont.drawString(g, "HI-SCORE", centerX, y0);
        
        score = format(data.getHighScore());
        width = mhFont.stringWidth(score);
        centerX = (MHDisplayModeChooser.getScreenSize().width/2 - (width/2));
        mhFont.drawString(g, score, centerX, y0+spacing);

    }
    
    private String format(int i)
    {
        String s = ""+i;
        if (i < 10)   s = "0" + s;
        if (i < 100)  s = "0" + s;
        if (i < 1000) s = "0" + s;
        
        return s;
    }
}


class LivesDisplay
{
    public LivesDisplay()
    {
    }
    
    public void render(Graphics2D g, MHFont mhFont)
    {
        SIDataModel data = SIDataModel.getInstance();
        int y = SIDataModel.getInstance().getGroundElevation();
        
        g.setColor(Color.GREEN);
        g.drawLine(0, y, MHDisplayModeChooser.getScreenSize().width, y);
        
        drawLives(g, 20, y, data.getLives(1), mhFont);
        
        if (data.getNumPlayers() == 2)
            drawLives(g, MHDisplayModeChooser.getScreenSize().width - 200, y, data.getLives(2), mhFont);
    }

    
    private void drawLives(Graphics2D g, int x, int y, int numLives, MHFont mhFont)
    {
        SIDataModel data = SIDataModel.getInstance();
        Image icon = data.getPlayerSprite();
        String lives = ""+numLives;
        mhFont.drawString(g, lives, x, y+mhFont.getHeight()+5);
        
        x += mhFont.stringWidth(lives) + 5;
        
        for (int i = 0; i < numLives-1; i++)
        {
            g.drawImage(icon, x, y+5, null);
            x += icon.getWidth(null) + 5;
        }
    }
}