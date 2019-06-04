/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise;

import com.jme3.math.Vector3f;

/**
 *
 * @author Tomasz.Naglik
 */
public class NoiseFilter {
    
    public NoiseSettings settings;
       
    public NoiseFilter (NoiseSettings settings){
        this.settings = settings;
    }
    
    public float evaluate(Vector3f point){
        
        float noiseValue = 0;
        float frequency = settings.baseRoughness;
        float amplitude = 1f;
        
        for(int i=0;i<settings.numLayers;i++){
            Vector3f newPoint = new Vector3f(point.add(settings.center).mult(frequency));
            float v = SimplexNoise.generateNoise(newPoint);
            noiseValue += (v+1)*0.5f*amplitude;
            noiseValue += v*amplitude;
            frequency *= settings.roughness;
            amplitude *= settings.persistance;
        }
        //noiseValue = Math.max(0.1f, noiseValue - settings.minValue);
        noiseValue = noiseValue - settings.minValue;
        return noiseValue * settings.strength;
    }
}
