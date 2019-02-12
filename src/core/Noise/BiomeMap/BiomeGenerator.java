/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.BiomeMap;

import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import core.Noise.NoiseFilter;
import core.Noise.SimplexNoise;
import core.Planet.Earth;
import core.Planet.MapNode;

/**
 *
 * @author Tomasz.Naglik
 */
public class BiomeGenerator {
    
    NoiseFilter[] noiseFilters;
    int masksIndex[];
    BiomeSettings biomeSettings;
    
    public BiomeGenerator( BiomeSettings settings){
        this.biomeSettings = settings;
        
            
        noiseFilters = new NoiseFilter[biomeSettings.noiseLayers.length];
        masksIndex = new int[biomeSettings.noiseLayers.length];
        
        for(int i = 0; i<noiseFilters.length;i++){
            noiseFilters[i] = new NoiseFilter(biomeSettings.noiseLayers[i].settings);
        }
        initializeNoiseFilters();
    }

    public Biome calculatePointOnPlanet(MapNode mapNode) {
        
        Biome biome = Biome.GRASSLAND;
        if(isDesert(mapNode))
            biome = Biome.DESERT;
        
        if(isCoast(mapNode))
            biome = Biome.BEACH; 
        if(isPolar(mapNode))
            biome = Biome.GLACIER;
        if(mapNode.height > 0.0051f )
            biome = Biome.SNOW_PEAKS;
        if(isHill(mapNode))
            biome = Biome.CLIFF;
        
        
        if(mapNode.height == 0)
            biome = Biome.OCEAN;
        
        
        return biome;
    }

    public float[] getColors(Biome biome) {
        
        Vector4f v = new Vector4f(0,0,0,0);
         if(biome == Biome.DESERT)
            v = new Vector4f(0.929f, 0.788f, 0.686f, 0f);
         
         if(biome == Biome.GRASSLAND)
            v = new Vector4f(89f/255f,149f/255f,74f/255f,0);
                
        if(biome == Biome.BEACH)
            v = new Vector4f(1f, 240f/255f, 201f/255f, 0f);
        
       
        if(biome == Biome.SNOW_PEAKS)
            v = new Vector4f(1f, 1f, 1f, 0f);
        if(biome == Biome.CLIFF)
            v = new Vector4f(96f/255f,96f/255f,100f/255f,0);
        if(biome == Biome.GLACIER)
            v = new Vector4f(1f, 1f, 1f, 0f);
        if(biome == Biome.OCEAN )
            v = new Vector4f(0f, 0.467f, 0.745f, 0f);
        if(biome == Biome.RIVER)
            v = new Vector4f(0f, 0.367f, 0.645f, 0f);
       
        
        float[] arr = new float[4];
        arr[0] = v.x;
        arr[1] = v.y;
        arr[2] = v.z;
        arr[3] = v.w;
        
        
        
        return arr;
    }
    
    public float[] getColors(float height){
        float[] arr = new float[4];
        arr[0] = height*255;
        arr[1] = height*255;
        arr[2] = height*255;
        arr[3] = height*255;
        
        return arr;
    }

    private boolean isCoast(MapNode mapNode) {
        boolean neighbourIsWater = false, neighbourIsLand = false;
        
        for(MapNode m : mapNode.neighbours){
            if(m.height <= 0)
                neighbourIsWater = true;
            if(m.height > 0)
                neighbourIsLand = true;
        }
        
        if(neighbourIsWater && neighbourIsLand && mapNode.height>0){
            Earth.coast.add(mapNode);
            return true;
        }
        
        return false;
    }

    private boolean isPolar(MapNode mapNode) {
        return Math.abs(mapNode.vertex.y)/Earth.SCALE - 0.95f - calculateNoiseAtPoint(mapNode.vertex,0) > 0; // * calculateNoiseAtPoint(mapNode.vertex)*2000) - (0.95f * Earth.SCALE)  > 0;
    }

    private boolean isHill(MapNode mapNode) {
        
        
        boolean result = mapNode.height > 0.0021f && mapNode.height < 0.0051f;
        if (result) {
            Earth.hills.add(mapNode);
            return true;
        }
        return false;
    }
    private boolean isDesert(MapNode mapNode) {
        //return Math.abs(mapNode.vertex.y)/Earth.SCALE - 0.55f - calculateNoiseAtPoint(mapNode.vertex) > 0.45f;
        float factor= ((0.1f-(Math.abs(mapNode.vertex.y)/Earth.SCALE)) -0.5f)*10;
        return (calculateNoiseAtPoint(mapNode.vertex,1)* factor)+0.49f < 0.6f 
                && (calculateNoiseAtPoint(mapNode.vertex,1)* factor)+0.49f  > 0.4f;
    }
    
    public float calculateNoiseAtPoint (Vector3f pointOnUnitSphere, int index){
        //float[] elevation = new float[noiseFilters.length];
        //float[] masks = new float[biomeSettings.noiseLayers.length];
        float elevationSum = 0;
        
        //calculate base values
        //for(int i=0; i<noiseFilters.length;i++)
             //elevation[i] = noiseFilters[i].evaluate(pointOnUnitSphere);
        /*
        //set masks
        for(int i=0; i<masks.length;i++)
            if(masksIndex[i] == -1)
                masks[i] = 1;
            else
                masks[i] = elevation[masksIndex[i]];
        
        //apply mask
        for(int i=0; i<noiseFilters.length;i++)
                elevation[i] *= masks[i];
        
        //calculate final elevation
        for(int i=0; i<noiseFilters.length;i++)
            if(biomeSettings.noiseLayers[i].enabled)
                elevationSum += elevation[i];
        
            */
        
        //return elevationSum;
        
        return noiseFilters[index].evaluate(pointOnUnitSphere);
    }
     private void initializeNoiseFilters() {
        //continents
        biomeSettings.noiseLayers[0].enabled = true;
        noiseFilters[0].settings.baseRoughness = 0.000450f;
        noiseFilters[0].settings.roughness = 2.4f;
        noiseFilters[0].settings.strength = 0.0251f;
        noiseFilters[0].settings.numLayers = 4;
        noiseFilters[0].settings.persistance = 0.775f;
        noiseFilters[0].settings.minValue = 0.3f;//biomeSettings.Sea_Level;
        noiseFilters[0].settings.center = new Vector3f(300,-300,320);
        masksIndex[0] = -1;
        
        
        //desert
        biomeSettings.noiseLayers[1].enabled = false;
        noiseFilters[1].settings.baseRoughness = 0.001525f;
        noiseFilters[1].settings.roughness = 0.855f;
        noiseFilters[1].settings.strength = 0.5f;
        noiseFilters[1].settings.numLayers = 8;
        noiseFilters[1].settings.persistance = 0.85f;
        noiseFilters[1].settings.minValue = 0.0f;
        noiseFilters[1].settings.center = new Vector3f(0,100,0);
        masksIndex[1] = 0;
        
        
        //mountains
        biomeSettings.noiseLayers[2].enabled = true;
        noiseFilters[2].settings.baseRoughness = 0.003f;
        noiseFilters[2].settings.roughness = 2.5f;
        noiseFilters[2].settings.strength = 7.5f;
        noiseFilters[2].settings.numLayers = 6;
        noiseFilters[2].settings.persistance = 0.675f;
        noiseFilters[2].settings.minValue = 0.4f;//biomeSettings.Sea_Level;
        noiseFilters[2].settings.center = new Vector3f(100,50,0);
        masksIndex[2] = 0;
        
        
        //extra
        biomeSettings.noiseLayers[3].enabled = false;
        noiseFilters[3].settings.baseRoughness = 2;
        noiseFilters[3].settings.roughness = 3;
        noiseFilters[3].settings.strength = 1;
        noiseFilters[3].settings.numLayers = 4;
        noiseFilters[3].settings.persistance = 0.5f;
        noiseFilters[3].settings.minValue = 0.0f;
        noiseFilters[3].settings.center = new Vector3f(0,0,0);
        masksIndex[3] = -1;
    }

    
    
}
