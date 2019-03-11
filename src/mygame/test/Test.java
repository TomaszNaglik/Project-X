/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import core.Statics.StaticAssets;
import core.game.Game;

/**
 *
 * @author Tomasz.Naglik
 */
public class Test extends SimpleApplication{
    
    private static Test app;
    
    
    private static void setSettings(Test app){
        app.showSettings = false;
        int w = 1600;
        int h = (int)(w*(9f/16f));
        AppSettings settings = new AppSettings(true);
        settings.setResolution(w, h);
        settings.setFullscreen(false);
        app.setSettings(settings);
    }
    
    Game game;
    Terrain map;
    DirectionalLight sun;
    
    public static void main(String[] args){
        app = new Test();
        setSettings(app);
        app.start();
    }
    
    @Override
    public void simpleInitApp(){
        
        cam.setLocation(new Vector3f(0,10,0));
        flyCam.setMoveSpeed(30);
        map = new Terrain(assetManager);
        
        sun = new DirectionalLight();
        sun.setDirection(cam.getLocation().mult(-1));
        sun.setColor(ColorRGBA.White);
        
        rootNode.addLight(sun);
        rootNode.attachChild(map) ;
    }
    
    @Override
    public void simpleUpdate(float tpf){
        //map.updateMesh(tpf);
    }
           
}
