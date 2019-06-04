/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.Planet.PlanetFeatures.PlanetFeaturesGenerator;
import core.Statics.StaticAssets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz.Naglik
 */
public class Earth extends Node{
    
    private static final Logger LOGGER = Logger.getLogger(Earth.class.getName());
    //Resolution and split variables should have the following property: (resolution+split-1)/split should give a full integer value
    public final int resolution;
    public final int split = 6;
    
    public static final int SCALE = 1000;
    public final boolean isSphere = true;
    float step;
    private boolean isLand;
    
    public final int longResolution;
    
    public ArrayList<PlanetFace> faces = new ArrayList<>();
    public ArrayList<MapNode> map = new ArrayList<>();
    public ArrayList<MapNode> topSquare = new ArrayList<>();
    public ArrayList<MapNode> midSquares = new ArrayList<>();
    public ArrayList<MapNode> bottomSquare = new ArrayList<>();
    
    public ArrayList<MapNode> coast = new ArrayList<>();
    public ArrayList<MapNode> hills = new ArrayList<>();
    
    public ArrayList<MapNode> square1 = new ArrayList<>();
    public ArrayList<MapNode> square2 = new ArrayList<>();
    public ArrayList<MapNode> square3 = new ArrayList<>();
    public ArrayList<MapNode> square4 = new ArrayList<>();
    
    public ArrayList<MapNode> subArrays [];
    
    public PlanetFeaturesGenerator planetFeaturesGenerator = new PlanetFeaturesGenerator();
    
    Timestamp t1, t2, t3, t4, t5, t6;
    
    public Earth(boolean isLand, int resolution) {
        
        this.isLand = isLand;
        this.resolution = resolution;
        this.step = 1f/(float)(resolution-1);
        this.longResolution = 4*resolution -4;
        if(isLand)
            StaticAssets.earth = this;
        else
            StaticAssets.sea = this;
        
        LOGGER.log(Level.INFO, "Beginning of Earth creation: {0}", t1 = new Timestamp(System.currentTimeMillis()));
        
        generateMapNodes();
        LOGGER.log(Level.INFO,"MapNodes Generated: {0} , + {1}", new Object[]{t2 = new Timestamp(System.currentTimeMillis()), t2.getTime() - t1.getTime()});
        
        parametrizeMapNodes();
        LOGGER.log(Level.INFO,"MapNodes parametrized: {0} , + {1}", new Object[]{t3 = new Timestamp(System.currentTimeMillis()), t3.getTime() - t2.getTime()});
        
        assignSidesToMapNodes();
        LOGGER.log(Level.INFO,"MapNodes sides assigned: {0} , + {1}", new Object[]{t4 = new Timestamp(System.currentTimeMillis()), t4.getTime() - t3.getTime()});
        //buildFeatures();
        
        LOGGER.log(Level.INFO,"Features built: {0} , + {1}", new Object[]{t5 = new Timestamp(System.currentTimeMillis()), t5.getTime() - t4.getTime()});
        
        buildPlanet();
        LOGGER.log(Level.INFO,"Planet built: {0} , + {1}", new Object[]{t6 = new Timestamp(System.currentTimeMillis()), t6.getTime() - t5.getTime()});
        LOGGER.log(Level.INFO,"Total time spent: {0}", t6.getTime() - t1.getTime());
        
        
        
    }

    public void update(){
        
        //check which faces should be visible
        this.detachAllChildren();
        for (PlanetFace face : faces){
            if(face.isInVisibleRange())
                this.attachChild(face);
        }
        
    }
    
    private void generateMapNodes() {
        
        createTopBottomSquare();
               
        createSides();  
    }
    
    private void createSides(){
        
        //add top edge
        for(int z = 0; z<resolution; z++)
            midSquares.add(topSquare.get(z*resolution));
        for(int x = 1; x<resolution; x++)
            midSquares.add(topSquare.get(resolution*(resolution-1)+x));
        for(int z = 1; z<resolution; z++)
            midSquares.add(topSquare.get(resolution*(resolution-z)-1));
        for(int x = resolution -2; x>0; x--)
            midSquares.add(topSquare.get(x));
        
        float nx=0,ny,nz=0;
        //fill out the center
        for (int y = 1; y < resolution-1; y++){
            for(int x = 0; x < 4*resolution-4; x++){
                if(x < resolution){
                    nx = -0.5f;
                    nz = (x*step - 0.5f);
                }
                if(resolution  <= x && x  < 2*resolution -1){
                    if(x>resolution && x%(resolution-1) == 0)
                        nx = (resolution-1)*step - 0.5f;
                    else
                        nx = ((x%(resolution-1))*step) - 0.5f;
                    nz = 0.5f;                        
                }
                if(2*resolution-1  <= x && x  < 3*resolution -2){
                    nx = 0.5f;
                    nz = 0.5f - (x%(2*resolution-2)*step);                        
                }
                if(3*resolution -2 <= x){
                    nx = 0.5f - ((x%(3*resolution-3))*step);
                    nz = -0.5f;                        
                }

                ny = 0.5f - y*step;
                Vector3f positionXYZ = new Vector3f(nx,ny,nz);
                Vector2f positionXY = new Vector2f(x,y);
                createPoint(positionXYZ, positionXY, midSquares);
                }
            }
        //last add bottom edge from bottom square
        for(int z = 0; z<resolution; z++)
            midSquares.add(bottomSquare.get(z*resolution));
        for(int x = 1; x<resolution; x++)
            midSquares.add(bottomSquare.get(resolution*(resolution-1)+x));
        for(int z = 1; z<resolution; z++)
            midSquares.add(bottomSquare.get(resolution*(resolution-z)-1));
        for(int x = resolution -2; x>0; x--)
            midSquares.add(bottomSquare.get(x));
    }
    
    private void createTopBottomSquare(){
        for(int z = 0 ; z < resolution ; z++){
            for(int x = 0 ; x < resolution ; x++)  {              
                generatePoint(x, 1,z,topSquare);
                generatePoint(x,-1,z,bottomSquare);
            }
        }
    }
    
    private void generatePoint(int x, int y, int z, ArrayList<MapNode> array){
            float nx = -0.5f + (x*step);
            float ny = y * 0.5f;
            float nz = -0.5f + (z*step);

            Vector3f positionXYZ = new Vector3f(nx,ny,nz);
            Vector2f positionXY = new Vector2f(x,z);
            createPoint(positionXYZ, positionXY, array);
    }
    
    private MapNode createPoint(Vector3f positionXYZ, Vector2f positionXY, ArrayList<MapNode> array){
        if (isSphere)
            positionXYZ.normalizeLocal();
        positionXYZ.multLocal(SCALE);

        MapNode mapNode = new MapNode(positionXYZ, positionXY, isLand);
        array.add(mapNode);
        map.add(mapNode);
        
        return mapNode;
        
    }

    private void parametrizeMapNodes() {
        //for each mapnode add 4 neighbours
        for( int i=0; i< topSquare.size(); i++){
            topSquare.get(i).calculateNeighbours(i,resolution, 1, topSquare);
            bottomSquare.get(i).calculateNeighbours(i,resolution, -1, bottomSquare);
        }
        for( int i=4*resolution -4; i< midSquares.size()-(4*resolution -4); i++){
            midSquares.get(i).calculateNeighbours(i,resolution, 0, midSquares);
        }
        LOGGER.log(Level.INFO,"Neighbours added");
        
        //for each mapnode add height information and adjust the vertex
        for(MapNode mapNode : map)
            mapNode.setParameters();
            
        
        //for each mapnode calculate distance to and height difference
    }

    private void buildPlanetFace(ArrayList<MapNode> array, boolean isAntiClockwise, int split) {
        
        
        //split the mesh into submeshes
        subArrays = splitArray(resolution, split, array);
        //introduce higher frequency
        for (ArrayList<MapNode> subArray : subArrays) {
            //increaseFaceResolution(subArray);
        }
        //build faces
        boolean draw = true;
        
        int smallRes = (int)(float)Math.sqrt(subArrays[0].size());
        for (ArrayList<MapNode> subArray : subArrays) {
            PlanetFace face = new PlanetFace(subArray, smallRes, smallRes, isAntiClockwise);
            faces.add(face);
            
            
        }
        
        
        //PlanetFace face = new PlanetFace(array, resolution, resolution, isAntiClockwise);
        //faces.add(face);
        
    }
    
    private int getSubArrayIndex(int i,int split){
        int index = (i%resolution)/split + split*(i/(resolution/split));
        index += split * (i/split/resolution);
        return index;
    }
    
    private void buildPlanet(){
        
        
        buildPlanetFace(topSquare,true, split);
        buildPlanetFace(square1,true, split);
        buildPlanetFace(square2,true, split);
        buildPlanetFace(square3,true, split);
        buildPlanetFace(square4,true, split);
        buildPlanetFace(bottomSquare,false, split);
        
    }

    private void assignSidesToMapNodes() {
        for(int i=0; i< midSquares.size(); i++){
            
            // first side
            if(i%longResolution < resolution){
                square1.add(midSquares.get(i));
                
            }
            
            // second side
            if(i%longResolution >= 1 * resolution - 1 && i%longResolution < 2 * resolution -1){
                square2.add(midSquares.get(i));
                
            }
            
            // third side
            if(i%longResolution >= 2 * resolution - 2 && i%longResolution < 3 * resolution -2){
                square3.add(midSquares.get(i));
                
            }
            
            // fourth side
            if(i%longResolution >= 3 * resolution - 3 ){
                square4.add(midSquares.get(i));
                if((i+1)%longResolution == 0)
                    square4.add(midSquares.get(i+1-longResolution));
                
            }
        }
    }

    private void buildFeatures() {
        this.planetFeaturesGenerator.generateRivers();
        this.attachChild(this.planetFeaturesGenerator);
    }

    private void increaseFaceResolution(ArrayList<MapNode> subArray) {
        //get resolution
        int res = (int)Math.sqrt(subArray.size());
        int marchingIndex = res+1;
        int newResolution = res * 2 - 1;
        for(int i=0; i< newResolution*(newResolution-1); i++){
            if((i+1) % newResolution != 0){
                
                MapNode origin = subArray.get(i);
                MapNode right = new MapNode(interpolateVf3(origin, subArray.get(i+1)), interpolateVf2(origin, subArray.get(i+1)), isLand);
                MapNode down = new MapNode(interpolateVf3(origin, subArray.get(i+res)), interpolateVf2(origin, subArray.get(i+res)), isLand);
                MapNode diag = new MapNode(interpolateVf3(origin, subArray.get(i+res+1)), interpolateVf2(origin, subArray.get(i+res+1)), isLand);
                
                
                
                subArray.add(i+1, right);
                subArray.add(i+marchingIndex++, down);
                subArray.add(i+marchingIndex, diag);
                i++;
            }else{
                i+= newResolution-1;
            }
        }
    }
    
    private Vector3f interpolateVf3(MapNode a, MapNode b){
        Vector3f c = a.vertex.add(b.vertex);
        c.divide(2);
        return c;
    }
    private Vector2f interpolateVf2(MapNode a, MapNode b){
        Vector2f c = a.positionWithinChunk.add(b.positionWithinChunk);
        c.divide(2);
        return c;
    }

    private ArrayList<MapNode>[] splitArray(int bigResolution, int split, ArrayList<MapNode> mapNodes) {
        
        //find first element of subArrays
        
        int firstIndices[] = new int[split*split];
        int arrayIndex = 0;
        int i = 0;
        int smallResolution = (bigResolution+split-1)/split;
        while (i<bigResolution*(bigResolution-1)){
            for(int j=0; j<split; j++){
                
                
                firstIndices[arrayIndex]=i;
                arrayIndex++;
                i+=smallResolution-1;
            }
            i+=(smallResolution-2)*bigResolution+1;
        }
        //create subArrays
        ArrayList<MapNode> arrays [] = new ArrayList[firstIndices.length];
        for(int p = 0; p< arrays.length; p++)
            arrays[p] = new ArrayList<>();
        
        for(int j=0; j < arrays.length; j++){
            int subArrayOffset = firstIndices[j];
            int localOffset = 0;
            
            //arrays[j].add(mapNodes.get(subArrayOffset+localOffset));
            //System.out.println(localOffset+subArrayOffset);
            //localOffset++;
            while(arrays[j].size() < smallResolution*smallResolution){
               for(int k=0; k<smallResolution ; k++){ 
                    arrays[j].add(mapNodes.get(subArrayOffset+localOffset)); 
                    localOffset++;
               }
               localOffset += (smallResolution-1)*(split-1);
               
            }
            
        }
        
        return arrays;
    }
    
}
