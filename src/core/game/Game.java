/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.game;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import core.Planet.Earth;
import core.Planet.Planet;
import core.Statics.StaticAssets;


/**
 *
 * @author Tomasz.Naglik
 */
public class Game {
    
    
    Planet planet;
    Earth earth;
    DirectionalLight sun;
    Input input;
    PlanetCamera planetCamera;
    
    
    public Game(SimpleApplication app){
        
        //planet = new Planet();
        earth = new Earth();
        planetCamera = new PlanetCamera(earth);
        input = new Input(planetCamera);
        sun = new DirectionalLight();
        sun.setDirection(StaticAssets.camera.getDirection());
        sun.setColor(ColorRGBA.White);
        app.getRootNode().attachChild(earth);
        app.getRootNode().addLight(sun);
        
       
        
    }
    
    public void update(float tpf){
        
        input.update(tpf);
        planetCamera.update(tpf);
        //earth.update();
        
        sun.setDirection(StaticAssets.camera.getDirection().mult(-1));
        
        
    }
    
    
}
