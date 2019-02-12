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
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Tomasz.Naglik
 */
public class Earth extends Node{
    
    public final int resolution = 400;
    public static final int SCALE = 1000;
    public final boolean isSphere = true;
    
    public final int longResolution = 4*resolution -4;
    
    public static ArrayList<PlanetFace> faces = new ArrayList<>();
    public static ArrayList<MapNode> map = new ArrayList<>();
    public static ArrayList<MapNode> topSquare = new ArrayList<>();
    public static ArrayList<MapNode> midSquares = new ArrayList<>();
    public static ArrayList<MapNode> bottomSquare = new ArrayList<>();
    
    public static ArrayList<MapNode> coast = new ArrayList<>();
     public static ArrayList<MapNode> hills = new ArrayList<>();
    
    public static ArrayList<MapNode> square1 = new ArrayList<>();
    public static ArrayList<MapNode> square2 = new ArrayList<>();
    public static ArrayList<MapNode> square3 = new ArrayList<>();
    public static ArrayList<MapNode> square4 = new ArrayList<>();
    
    public PlanetFeaturesGenerator planetFeaturesGenerator = new PlanetFeaturesGenerator();
    
    public Earth() {
        
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp1);
        generateMapNodes();
        
        parametrizeMapNodes();
        
        
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp1);
        long diff = timestamp2.getTime()-timestamp1.getTime();
        double ave = diff/map.size();
        System.out.println("time spent: "+diff);
        System.out.println("average time per node: "+ave);
        assignSidesToMapNodes();
        
        buildFeatures();
        buildPlanet();
        
    }

    public void update(){
        
    }
    
    private void generateMapNodes() {
        
        //create top square
        float step = 1f/(float)(resolution-1);
        
        for(int z = 0 ; z < resolution ; z++){
            for(int x = 0 ; x < resolution ; x++){
                
                float nx = -0.5f + (x*step);
                float nz = -0.5f + (z*step);
                
                Vector3f positionXYZ = new Vector3f(nx,0.5f,nz);
                Vector2f positionXY = new Vector2f(x,z);
                createPoint(positionXYZ, positionXY, topSquare);
            }
        }
        
               
        // create bottom square
        for(int z = 0 ; z < resolution ; z++){
            for(int x = 0 ; x < resolution ; x++){
                
                float nx = -0.5f + (x*step);
                float nz = -0.5f + (z*step);
                
                Vector3f positionXYZ = new Vector3f(nx,-0.5f,nz);
                Vector2f positionXY = new Vector2f(x,z);
                createPoint(positionXYZ, positionXY, bottomSquare);
            }
        }
        
        
        
        // create sides
            //first add edge from top square
            
            for(int z = 0; z<resolution; z++)
                midSquares.add(topSquare.get(z*resolution));
            for(int x = 1; x<resolution; x++)
                midSquares.add(topSquare.get(resolution*(resolution-1)+x));
            for(int z = 1; z<resolution; z++)
                midSquares.add(topSquare.get(resolution*(resolution-z)-1));
            for(int x = resolution -2; x>0; x--)
                midSquares.add(topSquare.get(x));
            
            
            float nx,ny,nz;
            //fill out the center
            for (int y = 1; y < resolution-1; y++){
                ny = 0.5f - y*step;
                int offset = 0;
                Vector3f positionXYZ = new Vector3f();
                
                for(int x = 0; x < 4*resolution-4; x++){
                    
                    if(x < resolution){
                        nx = -0.5f;
                        nz = (x*step - 0.5f);
                        positionXYZ = new Vector3f(nx,ny,nz);
                    }
                    offset = resolution-1;
                    if(resolution  <= x && x  < 2*resolution -1){
                        if(x>resolution && x%offset == 0)
                            nx = (resolution-1)*step - 0.5f;
                        else
                            nx = ((x%offset)*step) - 0.5f;
                        nz = 0.5f;                        
                        positionXYZ = new Vector3f(nx,ny,nz);
                    }
                    offset = 2*resolution-2;
                    if(2*resolution-1  <= x && x  < 3*resolution -2){
                        nx = 0.5f;
                        nz = 0.5f - (x%(offset)*step);                        
                        positionXYZ = new Vector3f(nx,ny,nz);
                    }
                    offset = 3*resolution-3;
                    if(3*resolution -2 <= x && x  < 4*resolution -4){
                        nx = 0.5f - ((x%(3*resolution-3))*step);
                        nz = -0.5f;                        
                        positionXYZ = new Vector3f(nx,ny,nz);
                    }
                    
                    
                    
                    Vector2f positionXY = new Vector2f(x,y);
                    MapNode mapNode = createPoint(positionXYZ, positionXY, midSquares);
                    
                    
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
    
    private MapNode createPoint(Vector3f positionXYZ, Vector2f positionXY, ArrayList<MapNode> array){
        if (isSphere)
            positionXYZ.normalizeLocal();
        positionXYZ.multLocal(SCALE);

        MapNode mapNode = new MapNode(positionXYZ, positionXY);
        array.add(mapNode);
        map.add(mapNode);
        
        return mapNode;
        
    }

    private void parametrizeMapNodes() {
        //for each mapnode add 4 neighbours
        for( int i=0; i< topSquare.size(); i++){
            topSquare.get(i).calculateNeighbours(i,resolution, 1, topSquare);
        }
        for( int i=0; i< bottomSquare.size(); i++){
            bottomSquare.get(i).calculateNeighbours(i,resolution, -1, bottomSquare);
        }
        for( int i=0; i< midSquares.size(); i++){
            
            midSquares.get(i).calculateNeighbours(i,resolution, 0, midSquares);
        }
        
        
        //for each mapnode add height information and adjust the vertex
        for(MapNode mapNode : map)
            mapNode.setParameters();
            
        
        //for each mapnode calculate distance to and height difference
    }

    private void buildPlanetFace(ArrayList<MapNode> array, boolean isAntiClockwise) {
        
        
        PlanetFace topFace = new PlanetFace(array, resolution, resolution, isAntiClockwise);
        faces.add(topFace);
        this.attachChild(topFace);
        
        
        
        PlanetFace bottomFace = new PlanetFace(bottomSquare, resolution, resolution, false);
        faces.add(bottomFace);
        this.attachChild(bottomFace);
        
        
    }
    
    private void buildPlanet(){
        buildPlanetFace(topSquare,true);
        buildPlanetFace(square1,true);
        buildPlanetFace(square2,true);
        buildPlanetFace(square3,true);
        buildPlanetFace(square4,true);
        buildPlanetFace(bottomSquare,false);
        
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
    
}
