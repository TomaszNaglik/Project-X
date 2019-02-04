/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.HeightMap;

import core.Noise.NoiseSettings;

/**
 *
 * @author Tomasz.Naglik
 */
public class ShapeSettings {
    public float planetRadius = 50;
    public NoiseLayer[] noiseLayers;
    
    
    public ShapeSettings(){
        noiseLayers = new NoiseLayer[4];
        for( int i=0; i< noiseLayers.length; i++)
            noiseLayers[i] = new NoiseLayer();
    }
    
    public class NoiseLayer{
        public boolean enabled = true;
        //public boolean useFirstLayerAsMask = false;
        public NoiseSettings settings;
        
        public NoiseLayer(){
            settings = new NoiseSettings();
        }
    }
}
