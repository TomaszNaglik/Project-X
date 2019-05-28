/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet.PlanetFeatures;

import com.jme3.scene.Node;
import core.Planet.MapNode;
import java.util.ArrayList;

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
           MapNode point = points.get(i);
           point.isRiver = true;
           point.updateBiome();
        }
    }
    
    public static River generateRiver(MapNode point){
        
        boolean isConfluence = false;
        boolean reachedOcean = false;
        boolean stopRiverGrowth = false;
        
        ArrayList<MapNode> mapNodes = new ArrayList<>();
       
        if(point.height > 0){
            
            mapNodes.add(point);
            MapNode nextMapNode = getNextNode(point, mapNodes);
            while ( !stopRiverGrowth){
                 if(nextMapNode == null || nextMapNode.isGlacier)
                    break;

                mapNodes.add(nextMapNode);

                if( nextMapNode.isRiver){
                    stopRiverGrowth = true;
                    isConfluence = true;
                }
                if( nextMapNode.isOcean ){
                    stopRiverGrowth = true;
                    reachedOcean = true;
                }

                nextMapNode = getNextNode(nextMapNode, mapNodes);
            }

            if(reachedOcean || isConfluence){
                for(MapNode m : mapNodes)
                    m.isRiver = true;
            }else{
                mapNodes.clear();
            }
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
