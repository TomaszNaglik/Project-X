/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Noise;

import com.jme3.math.Vector3f;
import java.util.Random;

/**
 *
 * @author Tomasz.Naglik
 */
public class HeightGenerator {
    
   /* public static final float SEA_LEVEL = 0.0021f;
    public static float maxHeight;
    
    
    public static Random rand = new Random(); 
    public static Random rand2 = new Random();
    public static float seedGeneral = rand2.nextFloat();//(float)Math.random()*(float)Math.random();
    public static float seedMountains = rand.nextFloat();//(float)Math.random()*(float)Math.random();
    
    public static float generateHeight(Vector3f point){
        
        float frequency = 0.03f; 
        float amplitude = 0.10f; 
        float persistance = 0.5f;
        int numOfLevels = 4;
        
        
        float height = 0;
        float continent = 0;
        float mountain =0;
        
        
        for(int i=0;i<numOfLevels;i++)
            continent += SimplexNoise.generateNoise(seedGeneral, point, frequency * (float)Math.pow(persistance, i))*Math.pow(persistance, i)*amplitude;
        continent = (float)Math.pow(continent, 2);
        
        //Mountains
        frequency = 50; //higher the value, smoother the terrain
        amplitude = 10f; //smaller the value, smaller the amplitude
        
        
        mountain += (1-Math.abs(SimplexNoise.generateNoise(seedGeneral, point, frequency)))*amplitude;
        mountain += (1-Math.abs(SimplexNoise.generateNoise(seedGeneral, point, frequency/4)*amplitude))*0.5;
        mountain += (1-Math.abs(SimplexNoise.generateNoise(seedGeneral, point, frequency/16)*amplitude))*0.3;
        mountain += (1-Math.abs(SimplexNoise.generateNoise(seedGeneral, point, frequency/64)*amplitude))*0.15;
        mountain += (1-Math.abs(SimplexNoise.generateNoise(seedGeneral, point,  frequency/128)*amplitude))*0.05;
        mountain *= continent;
        mountain = (float)Math.pow(mountain, 2);//*(float)Math.sin(point.x*point.y*point.z)/granularity;
        
        mountain = continent < SEA_LEVEL ? 0 : mountain;
        
        height = continent;
        if(height>maxHeight)
            maxHeight = height;*/
        //return Math.max(height, SEA_LEVEL);
    //}
}
