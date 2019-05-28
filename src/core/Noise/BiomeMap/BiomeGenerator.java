/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise.BiomeMap;

import com.jme3.math.Vector3f;
import core.Noise.BiomeMap.BiomeGenerators.*;
import core.Planet.Earth;
import core.Planet.MapNode;

/**
 *
 * @author Tomasz.Naglik
 */
public class BiomeGenerator {
    public static final BiomeSettings BIOME_SETTINGS = new BiomeSettings();
    
    public static OceanGenerator Ocean = new OceanGenerator(BIOME_SETTINGS);
    public static SeaGenerator Sea = new SeaGenerator(BIOME_SETTINGS);
    public static BeachGenerator Beach = new BeachGenerator(BIOME_SETTINGS);
    public static CliffGenerator Cliff = new CliffGenerator(BIOME_SETTINGS);
    public static DesertGenerator Desert = new DesertGenerator(BIOME_SETTINGS);
    public static PlainsGenerator Plains = new PlainsGenerator(BIOME_SETTINGS);
    public static GrasslandGenerator Grassland = new GrasslandGenerator(BIOME_SETTINGS);
    public static GlacierGenerator Glacier = new GlacierGenerator(BIOME_SETTINGS);
    public static Snow_PeaksGenerator Snow_Peaks = new Snow_PeaksGenerator(BIOME_SETTINGS);
    public static RiverGenerator River = new RiverGenerator(BIOME_SETTINGS);

    public static float[] generateBiomeSet(MapNode node) {
        
        float[] set = new float [18];
        Vector3f point = new Vector3f(node.vertex);
        
        set[0] = Ocean.calculatePointOnPlanet(point);
        set[1] = 0;//Sea.calculatePointOnPlanet(point);
        set[2] = Beach.calculatePointOnPlanet(point);
        set[3] = Cliff.calculatePointOnPlanet(point);
        set[4] = Desert.calculatePointOnPlanet(point);
        set[5] = Plains.calculatePointOnPlanet(point);
        set[6] = Grassland.calculatePointOnPlanet(point);
        set[7] = Glacier.calculatePointOnPlanet(point);
        set[8] = Snow_Peaks.calculatePointOnPlanet(point);
        set[9] = River.calculatePointOnPlanet(point);
        
        setIndex(6,set);
        
        if(isCoast(node)){
            setIndex(2, set);
        }
        if(node.height<=0){
            setIndex(0, set);
        }
        if(isHill(node)){
            setIndex(3, set);
        }
        if(isPeak(node)){
            setIndex(8, set);
        }
        
        
        return normalized(set);
        
        
    }
    
    public static float[] generateBiomeSet(Vector3f node) {
        
        float[] set = new float [18];
        Vector3f point = new Vector3f(node);
        
        set[0] = Ocean.calculatePointOnPlanet(point);
        set[1] = Sea.calculatePointOnPlanet(point);
        set[2] = Beach.calculatePointOnPlanet(point);
        set[3] = Cliff.calculatePointOnPlanet(point);
        set[4] = Desert.calculatePointOnPlanet(point);
        set[5] = Plains.calculatePointOnPlanet(point);
        set[6] = Grassland.calculatePointOnPlanet(point);
        set[7] = Glacier.calculatePointOnPlanet(point);
        set[8] = Snow_Peaks.calculatePointOnPlanet(point);
        
        
        
        
        return normalized(set);
        
        
    }
    
    public static void setIndex(int index, float[] set){
        for(int i = 0 ; i < set.length ; i++)
            set[i] = 0;
        set[index] = 1;
    }
    
    private static float[] normalized(float [] input){
        float [] result = new float [input.length];
        float sum = 0;
        for(int i = 0 ; i < input.length ; i++)
            sum += input[i];
        
        for(int i = 0 ; i < input.length ; i++)
            result[i] = input[i]/sum;
        return result;
    }
    
    private static boolean isCoast(MapNode mapNode) {
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
    
    private static boolean isHill(MapNode mapNode) {
        
        
        boolean result = mapNode.height > 0.0021f && mapNode.height < 0.0051f;
        if (result) {
            Earth.hills.add(mapNode);
            return true;
        }
        return false;
    }
    
    private static boolean isPeak(MapNode mapNode) {
                
        return mapNode.height >= 0.0051f;
    }
   
    
}
