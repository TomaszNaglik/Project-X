/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet.PlanetFeatures;

import com.jme3.scene.Node;
import core.Planet.MapNode;
import core.Statics.StaticAssets;
import java.util.ArrayList;

/**
 *
 * @author Tomasz.Naglik
 */
public class PlanetFeaturesGenerator extends Node{
    
    public static int RiverAmount = 400;
    
    public static ArrayList<River> rivers = new ArrayList<>();
    



    public void generateRivers(){
        
        ArrayList<MapNode> nodes = (ArrayList<MapNode>) StaticAssets.earth.map.clone();
        boolean riverFound = false;
        for(int i=0; i<RiverAmount ; i++){
            
            while(!riverFound){
                int index = (int)( (float)Math.random() * (nodes.size()-1));
                River river = River.generateRiver(nodes.get(index));
                if(river.points.size()>0)
                {
                    riverFound=true;
                    rivers.add(river);
                }
                else{
                    int size = nodes.size();
                    if(index > size -1)
                        System.out.println("STOP");
                    nodes.remove(index);
                }
            }
            riverFound = false;
        }
        
    }
}