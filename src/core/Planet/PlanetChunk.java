/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet;


//import core.Noise.HeightGenerator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import core.Noise.BiomeMap.Biome;
import core.Statics.StaticAssets;
import java.util.ArrayList;


 
public class PlanetChunk extends Node{
    
    private final static int LOD_INCREASE = 1;
    private final static int LOD_DEPTH = 2;
    private int LOD_SPLIT;
    private final static int MAX_LOD_SPLIT = 64;
    
    private float threshold_factor = 2.0f;
    public int SQR_LOD_SPLIT;
    public static int globalIndex = -1;
        
    public Boolean isRendered = Boolean.TRUE;
    public Boolean isInititalized = Boolean.FALSE;
    
    public Vector3f midPoint;
    
    public PlanetChunk parentChunk;
    public PlanetNew planet;
    public ArrayList<PlanetChunk> subChunks = new ArrayList<>();
    public int depth;
    private float LOD_Threshold;
    
    public int resolution;
    private int scale;
    private int index;
    
    private float offsetX, offsetZ;
    private float step;
    public Sides side;
    
    public MapNode[][] mapNodes;
    
    public final Vector3f[] vertices;
    private final Vector3f[] normals;
    
    private final int[] indices;
    private float [] colorArray;
    
    
    private Mesh mesh;
    
    
    
    
    
    
    
    public PlanetChunk(PlanetChunk parent, int index){
        
        globalIndex++;
        
        float square = (float)Math.sqrt(parent.LOD_SPLIT)+LOD_INCREASE;
        this.LOD_SPLIT = Math.min((int)(square*square), MAX_LOD_SPLIT);
        this.SQR_LOD_SPLIT = (int)Math.sqrt(LOD_SPLIT);
        
        this.index = index;
        this.parentChunk = parent;
        this.planet = parent.planet;
        this.side = parent.side;
        this.resolution = parent.resolution;
        this.scale = parent.scale;   
        
        this.step = parent.step/(LOD_SPLIT/SQR_LOD_SPLIT); 
        this.depth = parent.depth + 1;
        
        
        
        
        this.name = "Chunk"+globalIndex+this.side;
        this.LOD_Threshold  = setLODThreshold();
        
        
        this.vertices = new Vector3f[(resolution)*(resolution)];
        this.normals = new Vector3f[(resolution)*(resolution)];
        this.indices = new int[6*((resolution-1)*(resolution-1))];
        this.colorArray = new float[(resolution)*(resolution)*4];
        this.mapNodes = new MapNode[resolution][resolution];
        
        setOffset();
        setupMesh();
        setMidPoint();
        generateSubChunks();
        
        
    }
    
    
    public PlanetChunk (int i, int resolution, int scale, PlanetNew planet){
           
        globalIndex++;
        
        this.resolution = resolution;
        this.scale = scale;
        this.parentChunk = null;
        this.planet = planet;
        this.index = 0;
        this.step = 1.0f/(resolution-1);    
        this.depth = 1;
        this.LOD_SPLIT = 4;
        this.SQR_LOD_SPLIT = (int)Math.sqrt(LOD_SPLIT);
        
        
        switch (i){
            case (0): side = Sides.TOP; break;
            case (1): side = Sides.BOTTOM; break;
            case (2): side = Sides.LEFT; break;
            case (3): side = Sides.RIGHT; break;
            case (4): side = Sides.INFRONT; break;
            case (5): side = Sides.BEHIND; break;
        }
        this.name = "Chunk"+globalIndex+this.side;
        
                       
        this.vertices = new Vector3f[(resolution)*(resolution)];
        this.normals = new Vector3f[(resolution)*(resolution)];
        this.indices = new int[6*((resolution-1)*(resolution-1))];
        this.colorArray = new float[(resolution)*(resolution)*4];
        this.mapNodes = new MapNode[resolution][resolution];
        
        
        setOffset();
        setupMesh();
        setMidPoint();
        generateSubChunks();
    }
    
    
    
    public void update(){
        
        Vector3f cameraPos = new Vector3f(StaticAssets.camera.getLocation());
        Vector3f test = midPoint.subtract(cameraPos);
              
        isRendered = !(test.length() < LOD_Threshold && depth != LOD_DEPTH); 
    }
    private float setLODThreshold(){
        return ((float)scale/(float)depth)*threshold_factor; // 5*length;
    }
    private void setOffset(){
        float pX, pZ;
        
        if(parentChunk == null){
            pX=0; 
            pZ=0;
        } else{
            pX = parentChunk.offsetX;
            pZ = parentChunk.offsetZ;
        }
        // offset with regards to parent
        if(SQR_LOD_SPLIT == 0)
            SQR_LOD_SPLIT=1;
        
        offsetX = step*(index % SQR_LOD_SPLIT)*(resolution-1) + pX;
        offsetZ = step*(index / SQR_LOD_SPLIT)*(resolution-1) + pZ;
    }
    public final Vector3f setMidPoint(){
        return this.midPoint = vertices[(resolution*resolution)/2];
    }
    private void generateSubChunks(){
        
        if(depth < LOD_DEPTH){
               float square = (float)Math.sqrt(LOD_SPLIT)+LOD_INCREASE;
               float newSquare = (int)(square*square);
               for(int i=0; i<newSquare; i++){
                   PlanetChunk newChunk = new PlanetChunk(this,i);
                   subChunks.add(newChunk);
                   PlanetNew.chunks.add(newChunk); 
               }
             }
    }
    
    private void setupMesh(){
        
        for(int i = 0, triIndex = 0 ; i < vertices.length; i++ ){
             
            //Vertex coordinates
            Vector2f position = new Vector2f((int)i % (resolution),(int)i / (resolution));
            Vector3f point = new Vector3f(position.x * step + offsetX,0, position.y * step  + offsetZ);
            MapNode mapNode = new MapNode(point, position);
            mapNodes[(int)position.x][(int)position.y] = mapNode;
            
            
            
            vertices[i] = mapNode.vertex;
            //Triangle definition
            if (position.x != resolution - 1 && position.y != resolution - 1)
            {
                indices[triIndex] = i;
                indices[triIndex + 1] = i + resolution ;
                indices[triIndex + 2] = i + resolution + 1;

                indices[triIndex + 3] = i;
                indices[triIndex + 4] = i + resolution + 1;
                indices[triIndex + 5] = i  + 1;
                triIndex += 6;    
            }
        }     
    }
    
    public Mesh buildMesh(){
        
        for(int i=0 ; i < normals.length ; i++){
           normals[i] = mapNodes[i % resolution][i / resolution].normal;
        }
        
        generateColours();
        
        
        mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(normals));
        mesh.updateBound();
        
        Geometry geo = new Geometry(this.name+" Geometry", mesh); // using our custom mesh object
        Material matLight = new Material(StaticAssets.assetManager, "Common/MatDefs/Light/Lighting.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md"
        mesh.setBuffer(Type.Color, 4, colorArray);
        
        matLight.setBoolean("UseVertexColor", true);
        matLight.setBoolean("UseMaterialColors", true);
        //matLight.setColor("Ambient", ColorRGBA.White); //Using white here, but shouldn't matter that much
        //matLight.setColor("Diffuse", ColorRGBA.White);
        //matLight.setColor("Specular", ColorRGBA.White); //Using yellow for example
        matLight.setBoolean("VertexLighting", true);
        
        matLight.setFloat("Shininess", 0);

        geo.setMaterial(matLight);
        this.attachChild(geo);
        
        
        return mesh;
    }
    
     private void generateColours(){
        
        int colorIndex = 0;
        for(int i = 0; i < resolution; i++){
            for(int j = 0; j < resolution; j++){
                if(mapNodes[i][j].biome == Biome.GRASSLAND){
                    // Red value (is increased by .2 on each next vertex here)
                    colorArray[colorIndex++]= 0;
                    // Green value (is reduced by .2 on each next vertex)
                    colorArray[colorIndex++]= 0;
                    // Blue value (remains the same in our case)
                    colorArray[colorIndex++]= 1;
                    // Alpha value (no transparency set here)
                    colorArray[colorIndex++]= 1.0f;
                }else{
                    // Red value (is increased by .2 on each next vertex here)
                    colorArray[colorIndex++]= 0;
                    // Green value (is reduced by .2 on each next vertex)
                    colorArray[colorIndex++]= 1;
                    // Blue value (remains the same in our case)
                    colorArray[colorIndex++]= 0;
                    // Alpha value (no transparency set here)
                    colorArray[colorIndex++]= 1.0f;
                }   
            }
        }
            
     }     
}
