import java.awt.Graphics2D;
import java.util.Random;


public class SIAlien extends SISprite
{
    private int type, width = 0, height = 0;
    
    public SIAlien(int type)
    {
        this.type = type;
        setState(State.ALIVE);
    }
    
    
    public void render(Graphics2D g, int frame)
    {
        if (getState() == State.DYING)
        {
            g.drawImage(SIDataModel.getInstance().getBurstImage(), x, y, null);
            setState(State.DEAD);
        }
        else if (isAlive())
            g.drawImage(SIDataModel.getInstance().getAlienSprite(type, frame), x, y, null);
    }

    
    @Override
    public int getWidth()
    {
        if (width == 0)
            width = SIDataModel.getInstance().getAlienSprite(type, 0).getWidth(null);
                
        return width;
    }


    @Override
    public int getHeight()
    {
        if (height == 0)
            height = SIDataModel.getInstance().getAlienSprite(type, 0).getHeight(null);
                
        return height;
    }


    public boolean isAlive()
    {
        return getState() == State.ALIVE;
    }


    public void destroy()
    {
        if (isAlive())
        {
            setState(State.DYING);
            
            Random r = new Random();
            switch (r.nextInt(2))
            {
                case 0: SIAudio.play(SIAudio.ALIEN_HIT_1); break;
                case 1: SIAudio.play(SIAudio.ALIEN_HIT_2); break;
            }
            
        }
        // TODO:  Particles?
    }


    public int getType()
    {
        return type;
    }
    
}
