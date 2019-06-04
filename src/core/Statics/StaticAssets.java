/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Statics;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import core.Planet.Earth;



/**
 *
 * @author Tomasz.Naglik
 */
public class StaticAssets {
    
    public static SimpleApplication app;
    public static BitmapFont font;
    public static AssetManager assetManager;
    public static InputManager inputManager;
    public static FlyByCamera flyCamera;
    public static Camera camera;
    public static Earth earth;
    public static Earth sea;
    
    public static int viewThreshold = 5500;
    
    
    public static int screenHeight, screenWidth;
    
   
    
    //public StaticAssetManager(AssetManager manager){
    //    StaticAssetManager.manager = manager;
    //}
    
}
