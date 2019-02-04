package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.AnalogListener;
import com.jme3.system.AppSettings;
import core.Statics.StaticAssets;
import core.game.Game;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import com.jme3.math.Vector3f;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private static Main app;
    
    private static void setSettings(Main app) {
        app.showSettings = false;
        AppSettings settings = new AppSettings(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        StaticAssets.screenWidth= gd.getDisplayMode().getWidth();
        StaticAssets.screenHeight= gd.getDisplayMode().getHeight();
        settings.setResolution(StaticAssets.screenWidth, StaticAssets.screenHeight);
        settings.setFullscreen(false);
        
        
        
        app.setSettings(settings);
    }

    Game game;
    
    public static void main(String[] args) {
        app = new Main();
        setSettings(app);
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        StaticAssets.app = this;
        StaticAssets.font = this.guiFont;
        StaticAssets.assetManager = assetManager;
        StaticAssets.inputManager = inputManager;
        StaticAssets.flyCamera = flyCam;
        StaticAssets.camera = cam;
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(100);
        flyCam.setZoomSpeed(10);
        cam.setLocation(new Vector3f(0,0,1150));
        cam.setParallelProjection(false);
        
        game = new Game(app);
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        game.update(tpf);
        
    }
    
    

//    @Override
//    public void simpleRender(RenderManager rm) {
//        game.render();
//    }
}
