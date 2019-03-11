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
    
    private static void setIndex(int index, float[] set){
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
   /*

    public Biome calculatePointOnPlanet(MapNode mapNode) {
        
        Biome biome = Biome.GRASSLAND;
        //if(isDesert(mapNode))
           // biome = Biome.DESERT;
        
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
    
    */
     

    
    
}
