/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet;

import com.jme3.math.Vector2f;
import core.Noise.BiomeMap.BiomeGenerator;
import com.jme3.math.Vector3f;
import core.Noise.HeightMap.ShapeGenerator;
import core.Noise.HeightMap.ShapeSettings;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz.Naglik
 */
public class MapNode {
    
    
    private static final Logger LOGGER = Logger.getLogger(MapNode.class.getName());
    private static final  ShapeSettings SHAPE_SETTINGS = new ShapeSettings();
    private static final ShapeGenerator SHAPE_GENERATOR = new ShapeGenerator(SHAPE_SETTINGS);
    private static final float HEIGHT_DIFFERENCE_SCALE_FACTOR = 10000f;
    
    private static int mapNodeIndex = 0;
    public final int currentIndex;
    
    public Vector3f vertex;
    public Vector3f normal;
    public Vector2f positionWithinChunk;
    public float height;
    public float[] biomeSet;
    public MapNodeType mapNodeType;
    public MapNode[] neighbours; 
    public float[] distanceToNeighbour;
    public float[] heightDifferenceToNeighbour;
    
    
    public boolean isRiver = false;
    public boolean isGlacier = false;
    public boolean isOcean = false;
    
    
    
    
    
    
    public MapNode(Vector3f vertex, Vector2f positionWithinChunk){
        
        this.currentIndex = mapNodeIndex;
        mapNodeIndex++;
        this.positionWithinChunk = positionWithinChunk;
        this.vertex = vertex;
        
        height = SHAPE_GENERATOR.calculatePointOnPlanet(this.vertex);
        this.vertex.multLocal(1+height);
        
        
    }
    
    public void setParameters(){
        setBiome();
        
        setDistanceToNeighbours();
        setHeightDifferenceToNeighbours();
        setNormal();
        
    }
    
    public void setBiome(){
        biomeSet = BiomeGenerator.generateBiomeSet(this);
        if(biomeSet[7] == 1)
            isGlacier = true;
        if(biomeSet[0] == 1)
            isOcean=true;
    }
    public void updateBiome(){
        if(isRiver)
            BiomeGenerator.setIndex(9, biomeSet);
    }
    
    public MapNode getNeighbours(int i){
        if (i<neighbours.length && i>=0)
            return neighbours[i];
        LOGGER.log(Level.SEVERE, "MapNode getNeighbours(): passed an incorrect index {0}", i);
       return neighbours[0];
    }
    
    private void setDistanceToNeighbours(){
        
       
       if(neighbours.length == 4)
           if(neighbours[0] == null ||
            neighbours[1] == null ||
            neighbours[2] == null ||
            neighbours[3] == null )
                
            LOGGER.log(Level.SEVERE, "MapNode SetDistanceToNeighbouts STOP: no neighbour found");
                
       if(neighbours.length == 3)
           if(neighbours[0] == null ||
            neighbours[1] == null ||
            neighbours[2] == null )
               
            LOGGER.log(Level.SEVERE, "MapNode SetDistanceToNeighbouts STOP: no neighbour found");
       
       this.distanceToNeighbour = new float[neighbours.length];
        for(int i=0 ; i<distanceToNeighbour.length ; i++){
            distanceToNeighbour[i] = vertex.distance(neighbours[i].vertex) * HEIGHT_DIFFERENCE_SCALE_FACTOR;
        }
    }
    public float getDistanceToNeighbour(int i){
        if (i<distanceToNeighbour.length && i>=0)
            return distanceToNeighbour[i];
        LOGGER.log(Level.SEVERE, "MapNode getNeighbours(): passed an incorrect index {0}", i);
        return 0;
    }
    
    private void setHeightDifferenceToNeighbours(){
        this.heightDifferenceToNeighbour = new float[neighbours.length];
        for(int i=0 ; i<heightDifferenceToNeighbour.length ; i++){
            heightDifferenceToNeighbour[i] = (neighbours[i].height - height) *HEIGHT_DIFFERENCE_SCALE_FACTOR;
        }
    }
    public float getHeightDifferenceToNeighbour(int i){
        if (i<heightDifferenceToNeighbour.length && i>=0)
            return heightDifferenceToNeighbour[i];
        System.out.println("MapNode getHeightDifferenceToNeighbour(): passed an incorrect index " + i);
        return 0;
    }

    public void setNormal(){
        //source https://hub.jmonkeyengine.org/t/calculating-vertex-normals-of-custom-mesh/28179/4
        
        if(this.neighbours.length >3){
        
            Vector3f r1, r2, r3, r4;
            Vector3f v0 = new Vector3f(vertex); // Will store the vert you are calculating for
            Vector3f v1 = new Vector3f(neighbours[0].vertex); // 1st surrounding vert (in clockwise fashion)
            Vector3f v2 = new Vector3f(neighbours[1].vertex); // 2nd surrounding vert
            Vector3f v3 = new Vector3f(neighbours[2].vertex); // 3rd surrounding vert
            Vector3f v4 = new Vector3f(neighbours[3].vertex); // 4th surrounding vert

            // Next subtract the center vert (v0)
            v1.subtractLocal(v0);
            v2.subtractLocal(v0);
            v3.subtractLocal(v0);
            v4.subtractLocal(v0);

            // Next get the normalized cross product for each surrounding vert
            r1 = v1.cross(v2).normalize();
            r2 = v2.cross(v3).normalize();
            r3 = v3.cross(v4).normalize();
            r4 = v4.cross(v1).normalize();
       
            // Lastly, get the sum of the above cross products
            normal = new Vector3f();
            normal.set(r1);
            normal.addLocal(r2);
            normal.addLocal(r3);
            normal.addLocal(r4);
            
        }else{
            normal = this.vertex.mult(-1);
        }
        
        if(this.mapNodeType == MapNodeType.LowerEdge || this.mapNodeType == MapNodeType.LowerCenter)
            normal.multLocal(-1);
    }

    void calculateNeighbours(int i, int resolution, int type, ArrayList<MapNode> array) {
        // check if we are a corner
        
        int longResolution = 4*resolution -4;
        
        if(type == 1 && (i==0 || i == resolution-1 || i == resolution*(resolution-1) || i == resolution*resolution - 1)){
            if(i == 0){
                this.mapNodeType = MapNodeType.Corner;
                neighbours = new MapNode[3];
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = Earth.midSquares.get(longResolution);
            }
            if(i == resolution-1){
                this.mapNodeType = MapNodeType.Corner;
                neighbours = new MapNode[3];
                neighbours[0] = Earth.midSquares.get((3*resolution-3)+longResolution);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = array.get(i-1);
            }
            if(i == resolution*(resolution-1)){
                this.mapNodeType = MapNodeType.Corner;
                neighbours = new MapNode[3];
                neighbours[0] = array.get(i+1);
                neighbours[1] = Earth.midSquares.get(resolution - 1 + longResolution);
                neighbours[2] = array.get(i-resolution);
            }
            if(i == resolution*resolution - 1){
                this.mapNodeType = MapNodeType.Corner;
                neighbours = new MapNode[3];
                neighbours[0] =  Earth.midSquares.get(2*resolution-2+ longResolution);
                neighbours[1] = array.get(i-1);
                neighbours[2] = array.get(i-resolution);
            }
         }
        else if(type == -1 && (i==0 || i == resolution-1 || i == resolution*(resolution-1) || i == resolution*resolution - 1)){
            this.mapNodeType = MapNodeType.Corner;
            
            neighbours = new MapNode[3];
            if(i == 0){
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = Earth.midSquares.get(longResolution*(resolution-2));
            }
            if(i == resolution-1){
                neighbours[0] = Earth.midSquares.get((3*resolution-3)+(longResolution*(resolution-2)));
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = array.get(i-1);
            }
            if(i == resolution*(resolution-1)){
                neighbours[0] = array.get(i+1);
                neighbours[1] = Earth.midSquares.get(resolution - 1 + (longResolution*(resolution-2)));
                neighbours[2] = array.get(i-resolution);
            }
            if(i == resolution*resolution - 1){
                neighbours[0] =  Earth.midSquares.get(2*resolution-2 + (longResolution*(resolution-2)));
                neighbours[1] = array.get(i-1);
                neighbours[2] = array.get(i-resolution);
            }
        }
        //check if edge
        else if(type==1 &&(i<resolution || i > resolution*(resolution-1) || i%resolution ==0 || i%resolution == resolution -1)){
            this.mapNodeType = MapNodeType.UpperEdge;
            
            neighbours = new MapNode[4];

            if(i<resolution){
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = array.get(i-1);
                neighbours[3] = Earth.midSquares.get(longResolution-i+longResolution);
            }
            if(i > resolution*(resolution-1)){
                neighbours[0] = array.get(i+1);
                neighbours[1] = Earth.midSquares.get((i%resolution)+resolution-1+longResolution);
                neighbours[2] = array.get(i-1);
                neighbours[3] = array.get(i-resolution);                
            }
            if(i%resolution ==0){
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = Earth.midSquares.get(i/resolution+longResolution);
                neighbours[3] = array.get(i-resolution);               
            }
            if(i%resolution == resolution -1){
                neighbours[0] = Earth.midSquares.get((3*resolution-3)-(i/resolution)+longResolution);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = array.get(i-1);
                neighbours[3] = array.get(i-resolution);                
            }
        }    
        else if(type==-1 &&(i<resolution || i > resolution*(resolution-1) || i%resolution ==0 || i%resolution == resolution -1)){
            
            this.mapNodeType = MapNodeType.LowerEdge;
            neighbours = new MapNode[4];

            if(i<resolution){
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = array.get(i-1);
                neighbours[3] = Earth.midSquares.get(longResolution-i+(longResolution*(resolution-2)));
            }
            if(i > resolution*(resolution-1)){
                
                neighbours[0] = array.get(i+1);
                neighbours[1] = Earth.midSquares.get((i%resolution)+resolution-1+((resolution-2)*longResolution));
                neighbours[2] = array.get(i-1);
                neighbours[3] = array.get(i-resolution);                
            }
            if(i%resolution ==0){
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = Earth.midSquares.get(i/resolution+(longResolution*(resolution-2)));
                neighbours[3] = array.get(i-resolution);               
            }
            if(i%resolution == resolution -1){
                neighbours[0] = Earth.midSquares.get((3*resolution-3)-(i/resolution)+(longResolution*(resolution-2)));
                neighbours[1] = array.get(i+resolution);
                neighbours[2] = array.get(i-1);
                neighbours[3] = array.get(i-resolution);                
            }
        }
        //check middleSquare edge dateline
        else if(type==0 &&(i%longResolution == 0 || i%longResolution == longResolution-1 ) && i<longResolution*(resolution-1) && i > longResolution - 1){
            this.mapNodeType = MapNodeType.DateLine;
            
            if(i%longResolution == 0){
                neighbours = new MapNode[4];
                neighbours[0] = array.get(i+1);
                neighbours[1] = array.get(i+longResolution);
                neighbours[2] = Earth.midSquares.get(i+longResolution-1);
                neighbours[3] = array.get(i-longResolution);
            }
            if(i%longResolution == longResolution-1){
                neighbours = new MapNode[4];
                neighbours[0] = array.get(i-longResolution+1);
                neighbours[1] = array.get(i+longResolution);
                neighbours[2] = array.get(i-1);
                neighbours[3] = array.get(i-longResolution);
            }
        }
        //remaining cases (inside of area
        else if(type == 0 && i> longResolution-1 && i < longResolution*(resolution-1) && i%longResolution>0 && i%longResolution < longResolution-1){
            this.mapNodeType = MapNodeType.Center;
            neighbours = new MapNode[4];
            neighbours[0] = array.get(i+1);
            neighbours[1] = array.get(i+longResolution);
            neighbours[2] = array.get(i-1);
            neighbours[3] = array.get(i-longResolution);
        }
        else if(type != 0 &&(i> resolution-1 && i < resolution*(resolution-1) && i%resolution>0 && i%resolution < resolution-1)){
            this.mapNodeType = MapNodeType.Center;    
            if(type == -1)
                this.mapNodeType = MapNodeType.LowerCenter;
            neighbours = new MapNode[4];
            neighbours[0] = array.get(i+1);
            neighbours[1] = array.get(i+resolution);
            neighbours[2] = array.get(i-1);
            neighbours[3] = array.get(i-resolution);
        }
    }
}
