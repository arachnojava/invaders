import mhframework.MHAppLauncher;
import mhframework.MHGameApplication;
import mhframework.MHVideoSettings;


public class SIMain
{
    public static void main(final String args[])
    {
        final MHVideoSettings settings = new MHVideoSettings();
        settings.bitDepth = 32;
        settings.fullScreen = MHAppLauncher.showDialog(null, false);
        settings.displayWidth = 800;
        settings.displayHeight = 600;
        settings.windowCaption = "Invaders, Possibly From Space";
        settings.showSplashScreen = false;

        new MHGameApplication(new SIAttractLoopScreen(), settings);

        System.exit(0);
     }
}
