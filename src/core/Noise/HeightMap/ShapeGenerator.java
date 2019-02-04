/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.HeightMap;

import com.jme3.math.Vector3f;
import core.Noise.NoiseFilter;

/**
 *
 * @author Tomasz.Naglik
 */
public class ShapeGenerator {
    
    ShapeSettings shapeSettings;
    NoiseFilter[] noiseFilters;
    int masksIndex[];
    
    
    
    public ShapeGenerator (ShapeSettings shapeSettings)
    {
        this.shapeSettings = shapeSettings;
        
        noiseFilters = new NoiseFilter[shapeSettings.noiseLayers.length];
        masksIndex = new int[shapeSettings.noiseLayers.length];
        
        for(int i = 0; i<noiseFilters.length;i++){
            noiseFilters[i] = new NoiseFilter(shapeSettings.noiseLayers[i].settings);
        }
        initializeNoiseFilters();
        
        
        
    }
    
    public float calculatePointOnPlanet (Vector3f pointOnUnitSphere){
        float[] elevation = new float[noiseFilters.length];
        float[] masks = new float[shapeSettings.noiseLayers.length];
        float elevationSum = 0;
        
        //calculate base values
        for(int i=0; i<noiseFilters.length;i++)
             elevation[i] = noiseFilters[i].evaluate(pointOnUnitSphere);
        
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
            if(shapeSettings.noiseLayers[i].enabled)
                elevationSum += elevation[i];
        
            
        
        return elevationSum;
        
    }

    private void initializeNoiseFilters() {
        //continents
        shapeSettings.noiseLayers[0].enabled = true;
        noiseFilters[0].settings.baseRoughness = 0.000450f;
        noiseFilters[0].settings.roughness = 2.4f;
        noiseFilters[0].settings.strength = 0.00151f;
        noiseFilters[0].settings.numLayers = 8;
        noiseFilters[0].settings.persistance = 0.575f;
        noiseFilters[0].settings.minValue = 0.3f;//biomeSettings.Sea_Level;
        noiseFilters[0].settings.center = new Vector3f(3,-2,120);
        masksIndex[0] = -1;
        
        
        //mountain mask
        shapeSettings.noiseLayers[1].enabled = false;
        noiseFilters[1].settings.baseRoughness = 2.5f;
        noiseFilters[1].settings.roughness = 3.5f;
        noiseFilters[1].settings.strength = 0.05f;
        noiseFilters[1].settings.numLayers = 6;
        noiseFilters[1].settings.persistance = 0.5f;
        noiseFilters[1].settings.minValue = 0.0f;
        noiseFilters[1].settings.center = new Vector3f(0,0,0);
        masksIndex[1] = 0;
        
        
        //mountains
        shapeSettings.noiseLayers[2].enabled = true;
        noiseFilters[2].settings.baseRoughness = 0.003f;
        noiseFilters[2].settings.roughness = 2.5f;
        noiseFilters[2].settings.strength = 4.5f;
        noiseFilters[2].settings.numLayers = 6;
        noiseFilters[2].settings.persistance = 0.575f;
        noiseFilters[2].settings.minValue = 0.4f;//biomeSettings.Sea_Level;
        noiseFilters[2].settings.center = new Vector3f(0,5,0);
        masksIndex[2] = 0;
        
        
        //extra
        shapeSettings.noiseLayers[3].enabled = false;
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
