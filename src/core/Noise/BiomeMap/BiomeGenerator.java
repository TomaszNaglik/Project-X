/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.BiomeMap;

import com.jme3.math.Vector3f;

/**
 *
 * @author Tomasz.Naglik
 */
public class BiomeGenerator {
    
    BiomeSettings settings;
    
    public BiomeGenerator( BiomeSettings settings){
        this.settings = settings;
    }

    public Biome calculatePointOnPlanet(Vector3f vertex, float height) {
        
        Biome biome = Biome.GRASSLAND;
        
        if(height == 0)
            biome = Biome.OCEAN;
        if(height > 0 && height < 0.000006f)
            biome = Biome.BEACH;   
        
        return biome;
    }

    public float[] getColors(Biome biome) {
        
        float[] arr = new float[4];
        arr[0] = 0;
        arr[1] = 1;
        arr[2] = 0;
        arr[3] = 0;
        
        if(biome == Biome.BEACH){
            arr[0] = 1f;
            arr[1] = 240f/255f;
            arr[2] = 201f/255f;
            arr[3] = 0;
            
        }
        
        if(biome == Biome.OCEAN){
            arr[0] = 0f;
            arr[1] = 0.467f;
            arr[2] = 0.745f;
            arr[3] = 0;
            
        }
        
        if(biome == Biome.SEA){
            
            
        }
        
        
        
        
        return arr;
    }
    
}
