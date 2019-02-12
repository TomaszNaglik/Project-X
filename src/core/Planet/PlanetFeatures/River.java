/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet.PlanetFeatures;

import com.jme3.scene.Node;
import core.Planet.MapNode;
import java.util.ArrayList;
import core.Noise.BiomeMap.Biome;

/**
 *
 * @author Tomasz.Naglik
 */
public class River extends Node{
    
    public ArrayList<MapNode> points = new ArrayList<>();
   
    public River (ArrayList<MapNode> points){
        this.points = points;
        this.name = NameGenerator.nextRiverName();
        build();
    }
    
    public final void build(){
       for(int i = 0 ; i< points.size(); i++){
            points.get(i).biome = Biome.RIVER;
            points.get(i).colors = MapNode.BIOME_GENERATOR.getColors(Biome.RIVER);
        }
    }
    
    public static River generateRiver(MapNode point){
        
        boolean isConfluence = false;
        boolean reachedOcean = false;
        if(point.height <= 0)
            return null;
        
        ArrayList<MapNode> mapNodes = new ArrayList<>();
        boolean stopRiverGrowth = false;
        
        mapNodes.add(point);
        
        
        MapNode nextMapNode = getNextNode(point, mapNodes);
        if(nextMapNode == null || nextMapNode.biome == Biome.GLACIER)
                stopRiverGrowth = true;
        while ( !stopRiverGrowth){
            mapNodes.add(nextMapNode);
            
            
            if( nextMapNode.isRiver){
                stopRiverGrowth = true;
                isConfluence = true;
            }
            if( nextMapNode.biome == Biome.OCEAN ){
                stopRiverGrowth = true;
                reachedOcean = true;
            }
            nextMapNode.isRiver = true;
            nextMapNode = getNextNode(nextMapNode, mapNodes);
            
            if(nextMapNode == null || nextMapNode.biome == Biome.GLACIER)
                stopRiverGrowth = true;
        }
        
        if(!reachedOcean && !isConfluence ){
            for(MapNode m : mapNodes)
                m.isRiver = false;
            return null;
        }
            
        
        return new River(mapNodes);
    }
    
    private static MapNode getNextNode(MapNode currentMapNode, ArrayList<MapNode> mapNodes) {
        
        float min = 1000000f;
        int minIndex = -1;
        
        for(int i=0; i < currentMapNode.heightDifferenceToNeighbour.length; i++){
            if( currentMapNode.heightDifferenceToNeighbour[i] < min && !mapNodes.contains(currentMapNode.neighbours[i])){
                min = currentMapNode.heightDifferenceToNeighbour[i];
                minIndex = i;
            }
        }
        
        if(minIndex == -1)
            return null;
        return currentMapNode.neighbours[minIndex];
    }
    
    
    
}
