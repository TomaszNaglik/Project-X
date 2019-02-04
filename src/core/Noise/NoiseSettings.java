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
public class NoiseSettings {
    
    public float strength = 1;
    public int numLayers = 4;
    public float baseRoughness = 1;
    public float roughness = 3;
    public float persistance = 0.05f;
    public Vector3f center = new Vector3f(0,0,0);
    public float minValue = 0;
}
