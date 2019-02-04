/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Tomasz.Naglik
 */




public class Tile extends Node{
    
    public static final float HALFPI = (float)Math.PI/2f;
    public Vector2f position;
    public int size;
    public AssetManager assetManager;
    
    public TileType tileType;
    public int elevation;
    public int population;
    
    public boolean hasRoad;
    
    Geometry geom;
    
    
    
    
    public Tile (int x, int y,AssetManager assetManager, MapGenerator mapGenerator){
        
        this.position = new Vector2f(x,y);
        this.assetManager = assetManager;
        mapGenerator.setTile(this, x, y);
        generateObject();
        
    }
    
    public void rotate (){
        geom.rotate(0.01f,0,0);
    }
    
    
    
    
    private void generateObject (){
        Quad b = new Quad(1f, 1f);
        geom = new Geometry("Quad",b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(false);
        
        if(this.tileType == TileType.GRASSLAND)
                mat.setColor("Color", ColorRGBA.Green);
        if(this.tileType == TileType.SEA)
            mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position.x, 0, position.y);
        geom.rotate(-HALFPI,0,0);
        this.attachChild(geom);
        geom.setName("Cube");
        System.out.println("Tile name from Tile class: " + this.getChild(0).getName());
        
    }
    
    
    
    
}
