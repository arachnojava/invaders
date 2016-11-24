import java.awt.Image;
import mhframework.MHDisplayModeChooser;
import mhframework.media.MHResourceManager;


public class SIDataModel
{
    public static boolean DEBUG_MODE = false;
    public static String IMAGE_DIRECTORY = "images/";
    
    private static SIDataModel instance;
    private ScoreTable scoreTable;
    private Image[][] invaders;
    private Image mysteryShip, playerSprite,  burstImage, alienProjectile, playerProjectile;
    private SIGameStatus gameStatus;

    private SIDataModel()
    {
        scoreTable = new ScoreTable();
        loadAlienSprites();
        mysteryShip = MHResourceManager.loadImage(IMAGE_DIRECTORY + "Alien3.gif");
        playerSprite = MHResourceManager.loadImage(IMAGE_DIRECTORY + "Player.gif");
        burstImage = MHResourceManager.loadImage(IMAGE_DIRECTORY + "Burst.gif");
        alienProjectile = MHResourceManager.loadImage(IMAGE_DIRECTORY + "Bomb.gif");
        playerProjectile = MHResourceManager.loadImage(IMAGE_DIRECTORY + "Bullet.gif");
        
        gameStatus = new SIGameStatus();
    }
    
    
    
    public Image getAlienProjectile()
    {
        return alienProjectile;
    }

    
    public Image getPlayerProjectile()
    {
        return playerProjectile;
    }

    
    public Image getPlayerSprite()
    {
        return playerSprite;
    }
    

    public Image getBurstImage()
    {
        return burstImage;
    }
    

    public Image getAlienSprite(int alien, int frame)
    {
        if (alien == 3)
            return mysteryShip;
        
        return invaders[alien][frame];
    }
    
    
    private void loadAlienSprites()
    {
        invaders = new Image[3][2];
        
        for (int alien = 0; alien < invaders.length; alien++)
            for (int frame = 0; frame < invaders[alien].length; frame++)
                invaders[alien][frame] = MHResourceManager.loadImage(IMAGE_DIRECTORY + "Alien" + alien + frame + ".gif");
    }
    
    
    public int getPlayerScore(int playerNumber)
    {
        return gameStatus.getScore(playerNumber);
    }

    
    
    public int getLives(int playerNumber)
    {
        return gameStatus.getLives(playerNumber);
    }
    
    
    public int getWaveNumber()
    {
        return gameStatus.getWave();
    }
    
    
    public int getHighScore()
    {
        return gameStatus.getHighScore();
    }
    
    public static SIDataModel getInstance()
    {
        if (instance == null) 
            instance = new SIDataModel();
        
        return instance;
    }
    
    
    public int getScoreValue(int alienType)
    {
        return scoreTable.getScoreValue(alienType);
    }
    
    
    public void addPlayerScore(int player, int scoreIncrease)
    {
        gameStatus.addScore(player, scoreIncrease);
    }


    public int getNumPlayers()
    {
        return gameStatus.getNumPlayers();
    }


    public void setNumPlayers(int i)
    {
        gameStatus.setNumPlayers(i);
    }



    public int getGroundElevation()
    {
        return MHDisplayModeChooser.getScreenSize().height - 30;
    }



    public void subtractLife(int playerNumber)
    {
        gameStatus.addLives(playerNumber, -1);
    }
    
    
    public void addLife(int playerNumber)
    {
        gameStatus.addLives(playerNumber, 1);
    }


    public void setWaveNumber(int wave)
    {
        gameStatus.setWave(wave);
        
    }
}



class ScoreTable
{
    private int[] scores = new int[] {10, 20, 30};
    
    private int[] ufoScores = new int[] {50, 100, 150, 300};
    
    
    public int getScoreValue(int alienType)
    {
        if (alienType < scores.length)
            return scores[alienType];
        
        return ufoScores[(int)(Math.random()*ufoScores.length)];
    }
}