
public class SIGameStatus
{
    public static final int FREE_LIFE_SCORE = 1000;
    
    private int highScore = 0;
    private int numPlayers = 2;
    private int wave = 0;
    private SIPlayerStatus[] playerStatus;
    
    public SIGameStatus()
    {
        reset(numPlayers);
    }
    
    public void reset(int numPlayers)
    {
        wave = 0;
        this.numPlayers = numPlayers;
        
        playerStatus = new SIPlayerStatus[numPlayers];
        for (int i = 0; i < numPlayers; i++)
            playerStatus[i] = new SIPlayerStatus();
        
    }
    
    private int validateNumPlayers(int n)
    {
        if (n <= 1) 
            return 0;

        return 1;
    }
    
    
    public int getHighScore()
    {
        return highScore;
    }
    
    
    
    public int getWave()
    {
        return wave;
    }

    public void setWave(int wave)
    {
        this.wave = wave;
    }

    public int getNumPlayers()
    {
        return numPlayers;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public int getScore(int playerNumber)
    {
        if (playerNumber > numPlayers) return 0;
        
        return playerStatus[validateNumPlayers(playerNumber)].getScore();
    }


    public int getLives(int playerNumber)
    {
        return playerStatus[validateNumPlayers(playerNumber)].getLives();
    }

    
    public void addScore(int playerNumber, int points)
    {
        int newScore = playerStatus[validateNumPlayers(playerNumber)].getScore() + points;
        playerStatus[validateNumPlayers(playerNumber)].setScore(newScore);
        
        if (newScore > highScore)
            highScore = newScore;
        
        if (newScore - playerStatus[validateNumPlayers(playerNumber)].getLastFreeLifeScore() > FREE_LIFE_SCORE)
        {
            addLives(playerNumber, 1);
            playerStatus[validateNumPlayers(playerNumber)].setLastFreeLifeScore(newScore);
        }
    }

    
    public void addLives(int playerNumber, int lives)
    {
        int newLives = playerStatus[validateNumPlayers(playerNumber)].getLives() + lives;
        playerStatus[validateNumPlayers(playerNumber)].setLives(newLives);
    }

    public void setNumPlayers(int i)
    {
        reset(i);
    }

}
