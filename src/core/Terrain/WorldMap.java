/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Terrain;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 *
 * @author Tomasz.Naglik
 */
public class WorldMap extends Node{
    
    public int width;
    public int height;
    
    public Tile[][] map;
    
    public MapGenerator mapGenerator;
    public AssetManager assetManager;
    
    
    
    public WorldMap(int w, int h, AssetManager assetManager){
        
        this.width = w;
        this.height = h;
        map = new Tile[width][height];
        this.mapGenerator = new MapGenerator();
        this.assetManager = assetManager;
        generateNewMap();
        
    }

    private void generateNewMap() {
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                
                Tile tile = new Tile(x,y,assetManager, mapGenerator);
                this.attachChild(tile);
                
                map[x][y] = tile;
                mapGenerator.setTile(tile, x, y);
            }
        }
        
        
    }

    

    
    
    
    
}
