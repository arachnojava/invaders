
public class SIPlayerStatus
{
    private int score = 0;
    private int lives = 3;
    private int lastFreeLifeScore = 0;
    
    public int getScore()
    {
        return score;
    }
    public void setScore(int score)
    {
        this.score = score;
    }
    public int getLives()
    {
        return lives;
    }
    public void setLives(int lives)
    {
        this.lives = lives;
    }
    public void setLastFreeLifeScore(int lastFreeLifeScore)
    {
        this.lastFreeLifeScore = lastFreeLifeScore;
    }
    public int getLastFreeLifeScore()
    {
        return lastFreeLifeScore;
    }
}
