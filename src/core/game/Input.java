/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.game;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import core.Statics.StaticAssets;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;

/**
 *
 * @author Tomasz.Naglik
 */
public class Input {
    
    InputManager inputManager;
    PlanetCamera planetCamera;
    
    
    public Input(PlanetCamera planetCamera){
        
        this.inputManager = StaticAssets.inputManager;
        this.planetCamera = planetCamera;
        // You can map one or several inputs to one named action
        this.inputManager.addMapping("Up",  new KeyTrigger(KeyInput.KEY_W),  new KeyTrigger(KeyInput.KEY_UP));
        this.inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_A),  new KeyTrigger(KeyInput.KEY_LEFT));
        this.inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_D),  new KeyTrigger(KeyInput.KEY_RIGHT));
        this.inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S),  new KeyTrigger(KeyInput.KEY_DOWN));
        this.inputManager.addMapping("Zoom In", new KeyTrigger(KeyInput.KEY_Q),  new KeyTrigger(KeyInput.KEY_PGUP));
        this.inputManager.addMapping("Zoom Out", new KeyTrigger(KeyInput.KEY_Z),  new KeyTrigger(KeyInput.KEY_PGDN));
        
        this.inputManager.addMapping("Mouse Wheel Up",  new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        this.inputManager.addMapping("Mouse Wheel Down",  new MouseAxisTrigger(MouseInput.AXIS_WHEEL,false));
                                                        
        // Add the names to the action listener.
        
        this.inputManager.addListener(cameraAnalogListener, "Left", "Right", "Up", "Down", "Zoom In", "Zoom Out", "Mouse Wheel Up","Mouse Wheel Down");
    }
    
    
    public void update(float tpf){
        
        float border = 40;
        float width = StaticAssets.screenWidth;
        float height = StaticAssets.screenHeight;
        
        if(inputManager.getCursorPosition().y<border)
            planetCamera.move(Direction.DOWN, tpf, 1);
        if(inputManager.getCursorPosition().x<border)
           planetCamera.move(Direction.LEFT, tpf, 1); 
        if(inputManager.getCursorPosition().y>height - border)
            planetCamera.move(Direction.UP, tpf, 1);
        if(inputManager.getCursorPosition().x>width -border)
            planetCamera.move(Direction.RIGHT, tpf, 1);
        
    }
 
    
    private final AnalogListener cameraAnalogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
              
          
            
            
            if (name.equals("Up")) {
                //System.out.println(planetCamera.camVelocity.toString()+ " "+planetCamera.newCamPos.toString());
                planetCamera.move(Direction.UP, tpf, 1);
            }
            if (name.equals("Down")) {
                planetCamera.move(Direction.DOWN, tpf, 1);
            }
            if (name.equals("Left")) {
                planetCamera.move(Direction.LEFT, tpf, 1);
            }
            if (name.equals("Right")) {
                planetCamera.move(Direction.RIGHT, tpf, 1);
            }
            if (name.equals("Zoom In")) {
                planetCamera.move(Direction.FORWARD, tpf, 1);
            }
            if (name.equals("Zoom Out")) {
                planetCamera.move(Direction.BACKWARD, tpf, 1);
            }
            
            if (name.equals("Mouse Wheel Up")) {
               planetCamera.move(Direction.BACKWARD, tpf, 60);
            }
            if (name.equals("Mouse Wheel Down")) {
               planetCamera.move(Direction.FORWARD, tpf, 60);
            }
            
            
        }
    };
    
    
}
