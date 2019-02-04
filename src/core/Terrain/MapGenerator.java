/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Terrain;

/**
 *
 * @author Tomasz.Naglik
 */
public class MapGenerator {
 
    //private final OpenSimplexNoise MainNoise = new OpenSimplexNoise();
    
    public void setTile (Tile tile, int x,int y){
        
        
        tile.elevation = (int)((float)Math.random()*200f - 100f);
        
        
        
            tile.tileType = TileType.GRASSLAND;
        
        
        
    }
    
}
