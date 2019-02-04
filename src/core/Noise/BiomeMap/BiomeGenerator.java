/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.BiomeMap;

import com.jme3.math.Vector4f;
import core.Planet.MapNode;

/**
 *
 * @author Tomasz.Naglik
 */
public class BiomeGenerator {
    
    BiomeSettings settings;
    
    public BiomeGenerator( BiomeSettings settings){
        this.settings = settings;
    }

    public Biome calculatePointOnPlanet(MapNode mapNode) {
        
        Biome biome = Biome.GRASSLAND;
        
        if(mapNode.height == 0)
            biome = Biome.OCEAN;
        if(mapNode.height > 0 && mapNode.height < 0.0001f)
            biome = Biome.BEACH;   
        
        return biome;
    }

    public float[] getColors(Biome biome) {
        
        Vector4f v = new Vector4f(0,0,0,0);
        if(biome == Biome.GRASSLAND)
            v = new Vector4f(89f/255f,149f/255f,74f/255f,0);
                
        if(biome == Biome.BEACH)
            v = new Vector4f(1f, 240f/255f, 201f/255f, 0f);
        
        if(biome == Biome.OCEAN)
            v = new Vector4f(0f, 0.467f, 0.745f, 0f);
        
        
        float[] arr = new float[4];
        arr[0] = v.x;
        arr[1] = v.y;
        arr[2] = v.z;
        arr[3] = v.w;
        
        
        return arr;
    }
    
}
