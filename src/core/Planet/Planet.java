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
public class Planet extends Node {
    
    public final ArrayList<PlanetChunkOld> chunks = new ArrayList<>();;
    public final int resolution = 300;
    public final int scale = 1000;
    private final int NumberOfFaces = 6;
    public final boolean isSphere = true;
    
    public Planet (){
        for(int i=0; i<NumberOfFaces; i++){
            PlanetChunkOld newChunk = new PlanetChunkOld(i, resolution, scale, this);
            chunks.add(newChunk);
        } 
        for(PlanetChunkOld c: chunks)
            c.initialize();
        
        for(PlanetChunkOld c: chunks)
            c.initialize2();
        
        for(PlanetChunkOld c: chunks)
            recusriveInitialize(c);
        
        for(PlanetChunkOld c: chunks)
            recusriveInitialize2(c);
        
    }
    
    private void recusriveInitialize(PlanetChunkOld chunk){
       
        
        for(PlanetChunkOld subChunk : chunk.subChunks){
            subChunk.initialize();
            
        }
            
        for(PlanetChunkOld subChunk : chunk.subChunks)
            recusriveInitialize(subChunk);    
        
        
    }
    
    public void update(){
            
        this.detachAllChildren();
            
        //add all chunks with isRendered flag to the node.
        for(PlanetChunkOld chunk : chunks){
            recursiveUpdate(chunk, children);
        }
        
    }   
    
    private void recursiveUpdate(PlanetChunkOld chunk, SafeArrayList<Spatial> children){

        chunk.update();

        if(chunk.isRendered){
            this.attachChild(chunk);
        }else if(chunk.subChunks.size()>0){
            for(PlanetChunkOld subChunk : chunk.subChunks){
                recursiveUpdate(subChunk, children);
            }
        }
    }

    private void recusriveInitialize2(PlanetChunkOld chunk) {
        for(PlanetChunkOld subChunk : chunk.subChunks){
            subChunk.initialize2();
            
        }
            
        for(PlanetChunkOld subChunk : chunk.subChunks)
            recusriveInitialize2(subChunk);
    }
    
    
        
       
}
