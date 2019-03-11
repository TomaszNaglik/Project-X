/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.BiomeMap.BiomeGenerators;

import com.jme3.math.Vector3f;
import core.Noise.BiomeMap.BiomeSettings;
import core.Noise.NoiseFilter;
import core.Noise.NoiseSettings;

/**
 *
 * @author Tomasz.Naglik
 */
public class RiverGenerator extends NoiseSettings {
    
    BiomeSettings biomeSettings;
    NoiseFilter[] noiseFilters;
    
    public RiverGenerator (BiomeSettings biomeSettings)
    {
        this.biomeSettings = biomeSettings;
        noiseFilters = new NoiseFilter[biomeSettings.noiseLayers.length];
        for(int i = 0; i<noiseFilters.length;i++){
            noiseFilters[i] = new NoiseFilter(biomeSettings.noiseLayers[i].settings);
        }
        initializeNoiseFilters();
    }
    
    public float calculatePointOnPlanet (Vector3f pointOnUnitSphere){
        float[] elevation = new float[noiseFilters.length];
        
        float elevationSum = 0;
        
        //calculate base values
        for(int i=0; i<noiseFilters.length;i++)
             elevation[i] = noiseFilters[i].evaluate(pointOnUnitSphere);
        //calculate final elevation
        for(int i=0; i<noiseFilters.length;i++)
            if(biomeSettings.noiseLayers[i].enabled)
                elevationSum += elevation[i];
        
        return elevationSum;
    }

    private void initializeNoiseFilters() {
        //
        biomeSettings.noiseLayers[0].enabled = true;
        noiseFilters[0].settings.baseRoughness = 0.000850f;
        noiseFilters[0].settings.roughness = 2.4f;
        noiseFilters[0].settings.strength = 0.00151f;
        noiseFilters[0].settings.numLayers = 8;
        noiseFilters[0].settings.persistance = 0.475f;
        noiseFilters[0].settings.minValue = 1.35f;
        noiseFilters[0].settings.center = new Vector3f(300,-300,320);
        
        
        
        // mask
        biomeSettings.noiseLayers[1].enabled = false;
        noiseFilters[1].settings.baseRoughness = 2.5f;
        noiseFilters[1].settings.roughness = 3.5f;
        noiseFilters[1].settings.strength = 0.05f;
        noiseFilters[1].settings.numLayers = 6;
        noiseFilters[1].settings.persistance = 0.5f;
        noiseFilters[1].settings.minValue = 0.0f;
        noiseFilters[1].settings.center = new Vector3f(0,0,0);
        
        
        
        //
        biomeSettings.noiseLayers[2].enabled = true;
        noiseFilters[2].settings.baseRoughness = 0.0055f;
        noiseFilters[2].settings.roughness = 3.5f;
        noiseFilters[2].settings.strength = 4.5f;
        noiseFilters[2].settings.numLayers = 6;
        noiseFilters[2].settings.persistance = 0.575f;
        noiseFilters[2].settings.minValue = 1.3f;
        noiseFilters[2].settings.center = new Vector3f(100,50,0);
        
        
        
        //
        biomeSettings.noiseLayers[3].enabled = false;
        noiseFilters[3].settings.baseRoughness = 2;
        noiseFilters[3].settings.roughness = 3;
        noiseFilters[3].settings.strength = 1;
        noiseFilters[3].settings.numLayers = 4;
        noiseFilters[3].settings.persistance = 0.5f;
        noiseFilters[3].settings.minValue = 0.0f;
        noiseFilters[3].settings.center = new Vector3f(0,0,0);
        
    }
    
    
}
