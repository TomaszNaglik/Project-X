/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;

/**
 *
 * @author Tomasz.Naglik
 */
public class PlanetNew extends Node{
    
    public final int resolution = 3;
    public final int scale = 1000;
    private final int NumberOfFaces = 6;
    public final boolean isSphere = false;
    
    public static ArrayList<PlanetChunk> chunks = new ArrayList<>();
    public static ArrayList<MapNode> map = new ArrayList<>();
    public static ArrayList<MapNode> edgeMapNodes = new ArrayList<>();
    public static ArrayList<MapNode> middleCornerMapNodes = new ArrayList<>();
    public static ArrayList<MapNode> cornerMapNodes = new ArrayList<>();
    
    public PlanetNew (){
        for(int i=0; i<NumberOfFaces; i++){
            PlanetChunk newChunk = new PlanetChunk(i, resolution, scale, this);
            chunks.add(newChunk);
        } 
        
        
        
        
        
        for(MapNode mapNode : map)
            mapNode.setNeighbourParameters();
        
        for(PlanetChunk chunk : chunks){
            chunk.buildMesh();
        }
        
        
        
    }
    
    
    private void recursiveUpdate(PlanetChunk chunk, SafeArrayList<Spatial> children){

        chunk.update();

        if(chunk.isRendered){
            this.attachChild(chunk);
        }else if(chunk.subChunks.size()>0){
            for(PlanetChunk subChunk : chunk.subChunks){
                recursiveUpdate(subChunk, children);
            }
        }
    }
    public void update(){
            
        this.detachAllChildren();
            
        //add all chunks with isRendered flag to the node.
        for(PlanetChunk chunk : chunks){
            recursiveUpdate(chunk, children);
        }
        
    }   

    
    
    
}

    
