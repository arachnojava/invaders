import mhframework.media.MHResourceManager;
import mhframework.media.MHSoundManager;


public class SIAudio
{
    private static final String AUDIO_DIRECTORY = "audio/";
    private static MHSoundManager player = new MHSoundManager();
    
    public static final int PLAYER_FIRE = add("PlayerFire.wav");
    public static final int PLAYER_HIT  = add("PlayerDie.wav");
    public static final int SHIELD_HIT  = add("ShieldHit.wav");
    public static final int READY       = add("Ready.wav");
    public static final int UFO_FLY_BY  = add("UFOFlyBy1.wav");
    public static final int UFO_HIT     = add("UFOHit.wav");
    public static final int ALIEN_HIT_1 = add("Explosion0.wav");
    public static final int ALIEN_HIT_2 = add("Explosion1.wav");
    public static final int ALIEN_FIRE_1= add("AlienFire1.wav");
    public static final int ALIEN_FIRE_2= add("AlienFire2.wav");
    public static final int ALIEN_FIRE_3= add("AlienFire3.wav");
    
    
    private static int add(String filename)
    {
        return player.addSound(AUDIO_DIRECTORY + filename);
    }

    public static void play(int soundId)
    {
        player.play(soundId, false, MHSoundManager.AUTO_ASSIGN_CHANNEL);
    }

    
    public static void stop(int soundId)
    {
        player.stop(soundId);
    }
    
    
    
}
