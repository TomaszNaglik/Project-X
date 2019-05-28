/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.game;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import core.Planet.Earth;

import core.Statics.StaticAssets;


/**
 *
 * @author Tomasz.Naglik
 */
public class Game {
    
    
    
    Earth earth;
    DirectionalLight sun;
    Input input;
    PlanetCamera planetCamera;
    double r=0;
    
    
    public Game(SimpleApplication app){
        
       
        earth = new Earth();
        planetCamera = new PlanetCamera(earth);
        input = new Input(planetCamera);
        sun = new DirectionalLight();
        sun.setDirection(StaticAssets.camera.getLocation());
        sun.setColor(ColorRGBA.White);
        app.getRootNode().attachChild(earth);
        app.getRootNode().addLight(sun);
        System.out.println("Game initiated.");
       
        
        
    }
    
    public void update(float tpf){
        
        input.update(tpf);
        planetCamera.update(tpf);
        earth.update();
        // r -= 0.2*tpf;
        //Vector3f lightDirection = new Vector3f(StaticAssets.camera.getLocation().mult(1));
        //Vector3f finalDirection = new Vector3f(lightDirection.x*(float)Math.cos(r) + lightDirection.z*(float)Math.sin(r),lightDirection.y,-lightDirection.x*(float)Math.sin(r) + lightDirection.z*(float)Math.cos(r) );
        sun.setDirection(StaticAssets.camera.getLocation());
        
        
    }
    
    
}
