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
import core.Noise.BiomeMap.BiomeSettings;
import core.Noise.HeightGenerator;
import core.Noise.HeightMap.ShapeGenerator;
import core.Noise.HeightMap.ShapeSettings;
import core.Statics.StaticAssets;
import java.util.ArrayList;

/**
 *
 * @author Tomasz.Naglik
 */
public class PlanetChunkOld extends Node{
    
    private final static ShapeSettings SHAPE_SETTINGS = new ShapeSettings();
    private final static BiomeSettings BIOME_SETTINGS = new BiomeSettings();
    private static ShapeGenerator shapeGenerator = new ShapeGenerator(SHAPE_SETTINGS);
    
    
    
    private final static float PI = (float) Math.PI;
    private final static float HALFPI = (float) Math.PI/2f;
    private final static float THREEHPI = 3f*(float) Math.PI/2f;
    private final static int MAX_LOD_SPLIT = 64; //can be eliminated or improved upon
    private final static int LOD_INCREASE = 1;
    private final static int LOD_DEPTH = 2;
    private int LOD_SPLIT;
    
    private float threshold_factor = 2.0f;
    public int SQR_LOD_SPLIT;
    public static int globalIndex = -1;
        
    public Boolean isRendered = Boolean.TRUE;
    public Boolean isInititalized = Boolean.FALSE;
    
    public Vector3f midPoint;
    
    public PlanetChunkOld parentChunk;
    public Planet planet;
    public ArrayList<PlanetChunkOld> subChunks = new ArrayList<>();
    private int depth;
    private float LOD_Threshold;
    
    private int resolution;
    private int scale;
    private int index;
    
    private float offsetX, offsetZ;
    private float step;
    private Sides side;
    
    private ColorRGBA color;
    
    private PlanetChunkOld neighbors [];
    
    public final Vector3f[] vertices;
    private final Vector3f[] normals;
    private final Vector2f[] texCoords;
    private final int[] indices;
    
    private float [] heights;
    private float [] colorArray;
    
    private  Vector3f[][] nodes;
    
    private Mesh mesh;
    
    
    
    
    
    public PlanetChunkOld(PlanetChunkOld parent, int index, ColorRGBA color){
        globalIndex++;
        
        //increase of LOD split with every level down
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
        this.color = color;
        
        this.name = "Chunk"+globalIndex+this.side;
       
        this.LOD_Threshold  = setLODThreshold();
        
        
        this.vertices = new Vector3f[(resolution)*(resolution)];
        this.normals = new Vector3f[(resolution)*(resolution)];
        this.texCoords = new Vector2f[(resolution)*(resolution)];
        this.indices = new int[6*((resolution-1)*(resolution-1))];
        this.heights = new float[(resolution)*(resolution)];
        this.colorArray = new float[(resolution)*(resolution)*4];
        this.nodes = new Vector3f[this.vertices.length][4];
        setupMesh();
        generateSubChunks();
        
        
    }
    
    
    public PlanetChunkOld (int i, int resolution, int scale, Planet planet){
                
        globalIndex++;
        
        this.resolution = resolution;
        this.scale = scale;
        this.parentChunk = null;
        this.planet = planet;
        this.index = 0;
        this.step = 1.0f/(resolution-1);    
        this.depth = 1;
        this.color = ColorRGBA.White;
        this.LOD_SPLIT = 4;
        this.SQR_LOD_SPLIT = (int)Math.sqrt(LOD_SPLIT);
        this.color = ColorRGBA.Green;
        
        
        this.LOD_Threshold = setLODThreshold();
        
        switch (i){
            case (0): side = Sides.TOP; break;
            case (1): side = Sides.BOTTOM; break;
            case (2): side = Sides.LEFT; break;
            case (3): side = Sides.RIGHT; break;
            case (4): side = Sides.INFRONT; break;
            case (5): side = Sides.BEHIND; break;
        }
        this.name = "Chunk"+globalIndex+this.side;
        //if(this.side == Sides.INFRONT)
        //    this.color = ColorRGBA.Red;
                       
        this.vertices = new Vector3f[(resolution)*(resolution)];
        this.normals = new Vector3f[(resolution)*(resolution)];
        this.texCoords = new Vector2f[(resolution)*(resolution)];
        this.indices = new int[6*((resolution-1)*(resolution-1))];
        this.heights = new float[(resolution)*(resolution)];
        this.colorArray = new float[(resolution)*(resolution)*4];
        
        this.nodes = new Vector3f[this.vertices.length][4];
        
        
        
        setupMesh();
        generateSubChunks();
        
        
        
    }
    
    public void initialize(){
        
        populateNeighbours();
        /*System.out.println("");
        System.out.println(this.name);
        System.out.println("east: " +this.neighbors[0]);
        System.out.println("south: " +this.neighbors[1]);
        System.out.println("west: " +this.neighbors[2]);
        System.out.println("north: " +this.neighbors[3]);
        System.out.println("");*/
        
        //populateNodes();
    }
    
     void initialize2() {
        this.generateHeights();
        this.generateColors();
        this.mesh = buildMesh();
    }
    
     public void update(){
        
        Vector3f cameraPos = new Vector3f(StaticAssets.camera.getLocation());
        Vector3f test = midPoint.subtract(cameraPos);
              
        isRendered = !(test.length() < LOD_Threshold && depth != LOD_DEPTH); 
    }
    
    
    private void generateSubChunks(){
        
        if(depth < LOD_DEPTH){
               float square = (float)Math.sqrt(LOD_SPLIT)+LOD_INCREASE;
               float newSquare = (int)(square*square);
               for(int i=0; i<newSquare; i++){
                   PlanetChunkOld newChunk = new PlanetChunkOld(this,i, ColorRGBA.Green);
                   subChunks.add(newChunk);
               }
             }
    }
    
    private void populateNeighbours(){
        
        PlanetChunkOld [] n = new PlanetChunkOld[4];
               
        if(depth != 1){
            int edgeLength = this.SQR_LOD_SPLIT;
            int x = this.index % edgeLength;
            int y = this.index / edgeLength;
            int parentIndex;
            
            //right edge, need to get top neighor
            if(x==edgeLength-1){
                n[0] = findNeighboringChunk(this.parentChunk.neighbors, this.vertices[2*resolution-1]);
            } else {
                n[0] = this.parentChunk.subChunks.get(this.index +1);
            }
            
            //bottom edge, bottom neighbor
            if(y==edgeLength-1){                
                n[1] = findNeighboringChunk(this.parentChunk.neighbors, this.vertices[resolution*resolution-2]);
            }else{
                n[1] = this.parentChunk.subChunks.get(this.index + edgeLength);
            }
            
            //left edge, left neighbor
            if(x==0){
                n[2] = findNeighboringChunk(this.parentChunk.neighbors, this.vertices[resolution]);
            }else{
                n[2] = this.parentChunk.subChunks.get(this.index-1);
            }
            
            //top edge, top neighbor
            if(y==0){
                n[3] = findNeighboringChunk(this.parentChunk.neighbors, this.vertices[1]);
            }else{
                n[3] = this.parentChunk.subChunks.get(this.index - edgeLength);
            }
            
            
        }
        
        
        if(depth == 1){
            if(this.side == Sides.TOP){ // right, front, left, back   3,4,2,5
                
                n[0] = this.planet.chunks.get(3);
                n[1] = this.planet.chunks.get(4);
                n[2] = this.planet.chunks.get(2);
                n[3] = this.planet.chunks.get(5);
            }
            
            if(this.side == Sides.BOTTOM){ //right, back, left, front 
                
                n[0] = this.planet.chunks.get(3);
                n[1] = this.planet.chunks.get(5);
                n[2] = this.planet.chunks.get(2);
                n[3] = this.planet.chunks.get(4);
            }
            
            if(this.side == Sides.INFRONT){ //right, bottom, left, top
                
                n[0] = this.planet.chunks.get(3);
                n[1] = this.planet.chunks.get(1);
                n[2] = this.planet.chunks.get(2);
                n[3] = this.planet.chunks.get(0);
            }
            
            if(this.side == Sides.BEHIND){ // left, bottom, right, top
                
                n[0] = this.planet.chunks.get(3);
                n[1] = this.planet.chunks.get(0);
                n[2] = this.planet.chunks.get(2);
                n[3] = this.planet.chunks.get(1);
            }
            
            if(this.side == Sides.LEFT){ // back, top front, bottom
                
                n[0] = this.planet.chunks.get(0);
                n[1] = this.planet.chunks.get(4);
                n[2] = this.planet.chunks.get(1);
                n[3] = this.planet.chunks.get(5);
            }
            
            if(this.side == Sides.RIGHT){ //back, bottom , front, top
                
                n[0] = this.planet.chunks.get(1);
                n[1] = this.planet.chunks.get(4);
                n[2] = this.planet.chunks.get(0);
                n[3] = this.planet.chunks.get(5);
            }
            
        }
        
        this.neighbors = n;
    }
    
    private void generateColors(){
        
        int colorIndex = 0;
        for(int i = 0; i < this.heights.length; i++){
            
            if(heights[i] <= BIOME_SETTINGS.Sea_Level){
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
    
    private void populateNodes() {
        
        
            
        Vector3f v1, v2, v3, v4;
        for(int i=0 ; i<this.vertices.length;i++){
            int newIndex, nx,ny;
            int x = i % resolution;
            int y = i / resolution;
            //east vertex
            if(x==resolution -1){

                newIndex = findNeighboringVertexInAnotherChunk(i,1);
                if(newIndex == -1)
                    System.out.println("Chunk: " + this.depth+this.side + "   Direction: East   searched neighbour " + this.neighbors[0].side);
                v1 = new Vector3f(this.neighbors[0].vertices[newIndex]);
            }
            else
                v1 = new Vector3f(vertices[i+1]);

            //south vertex
            if(y==resolution -1){
                newIndex = findNeighboringVertexInAnotherChunk(i,0);
                if(newIndex == -1)
                    System.out.println("Chunk: " + this.depth+this.side + "   Direction: South   searched neighbour " + this.neighbors[1].side);
                v2 = new Vector3f(this.neighbors[0].vertices[newIndex]);
            }
            else
                v2 = new Vector3f(vertices[i+resolution]);
            //west vertex
            if(x==0){
                newIndex = findNeighboringVertexInAnotherChunk(i,1);
                if(newIndex == -1){
                    System.out.println("Chunk: " + this.depth+this.side + "   Direction: West   searched neighbour " + this.neighbors[2].side);
                    newIndex = findNeighboringVertexInAnotherChunk(i,1);
                }
                v3 = new Vector3f(this.neighbors[0].vertices[newIndex]);
            }
            else
                v3 = new Vector3f(vertices[i-1]);
            //north vertex
            if(y==0){
                newIndex = findNeighboringVertexInAnotherChunk(i,0);
                if(newIndex == -1)
                    System.out.println("Chunk: " + this.depth+this.side + "   Direction: North   searched neighbour " + this.neighbors[3].side);
                v4 = new Vector3f(this.neighbors[0].vertices[newIndex]);
            }
            else
                v4 = new Vector3f(vertices[i-resolution]);
            
            nodes[i][0] = v1;
            nodes[i][1] = v2;
            nodes[i][2] = v3;
            nodes[i][3] = v4;
        }
        
        
    }

    private void setupMesh(){
        
        
        setOffset();
        //System.out.println("SetupMesh: Chunk: "+ this.name + "  ");
        for(int i = 0, triIndex = 0 ; i < vertices.length; i++ ){
             
            //Vertex coordinates
            int gx = i % (resolution);
            int gz = i / (resolution);
            Vector3f point = new Vector3f(gx * step + offsetX,0, gz * step  + offsetZ);
            vertices[i] = positionVertex(point, this.side);
            //System.out.println("Vertx"+ i + ":  " + vertices[i].toString());
                        
            //Texture coordinates
            float tx = (float)(i%(resolution))/(resolution);
            float ty = (float)(i/(resolution))/(resolution);
            texCoords[i] = new Vector2f (tx, ty);
            
            //Triangle definition
            if (gx != resolution - 1 && gz != resolution - 1)
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
    
    private Mesh buildMesh(){
        
        for(int i=0 ; i < normals.length ; i++){
           normals[i] = calculateNormal(i);
        }
        
        mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
        mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(normals));
        mesh.updateBound();
        
        Geometry geo = new Geometry(this.name+" Geometry", mesh); // using our custom mesh object
        Material matLight = new Material(StaticAssets.assetManager, "Common/MatDefs/Light/Lighting.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md"
        mesh.setBuffer(Type.Color, 4, colorArray);
        
        
        //matLight.setFloat("Shininess", 15f);
        
        matLight.setBoolean("UseVertexColor", true);
        matLight.setBoolean("UseMaterialColors", true);
        matLight.setColor("Ambient", ColorRGBA.White); //Using white here, but shouldn't matter that much
        matLight.setColor("Diffuse", ColorRGBA.White);
        matLight.setColor("Specular", ColorRGBA.White); //Using yellow for example
        matLight.setBoolean("VertexLighting", true);
        //matLight.setBoolean("VertexColor", true);
        /*matLight.setBoolean("UseMaterialColors", true);
        matLight.setColor("Ambient", this.color);
        matLight.setColor("Diffuse", this.color);
        matLight.setColor("Specular", this.color);
        matLight.setFloat("Shininess", 0);*/

        geo.setMaterial(matLight);
        this.attachChild(geo);
        setMidPoint();
        
        return mesh;
    }
    
    
     private int findVertexIndex(Vector3f [] arr, Vector3f t){
        
        float test = 0.0001f;
        for(int i=0; i<arr.length;i++){
            Vector3f v = arr[i];
            if(Math.abs(v.x-t.x) < test && Math.abs(v.y - t.y) < test && Math.abs(v.z - t.z)< test)
                return i;
        }
        String n = this.name;
        return -1;
    }
    
    private int findNeighboringVertexInAnotherChunk(int i, int dir){
        
        int newIndex = -1;
        int nx,ny;
        int offset = 1;
        
        for(PlanetChunkOld chunk : this.neighbors){
            //System.out.println("");
            //System.out.println("New check");
            //System.out.println(this.vertices[i].toString());
            //System.out.println("Against");
            for(int j=0;j<this.neighbors.length;j++){
                if(chunk.vertices[j]==null)
                    System.out.println(chunk.vertices[j].toString());
            }
            
            
            newIndex = findVertexIndex(chunk.vertices, this.vertices[i]); //find the corresponding vertex in the neighbor chunk
            if(newIndex!=-1) // possible problem at planet corners
                break;
            
        }
            //System.out.println("Found");
            //System.out.println("");
            nx = newIndex % resolution;
            ny = newIndex / resolution;
            if(nx == 0 && dir == 1)
                newIndex = (nx+offset) + (ny*resolution);
            if(ny == 0 && dir == 0)
                newIndex = (nx) + ((ny+offset)*resolution);
            if(nx == resolution -1 && dir == 1)
                newIndex = (nx-offset) + (ny*resolution);
            if(ny == resolution -1 && dir == 0)
                newIndex = (nx) + ((ny-offset)*resolution);
            
            return newIndex;
    }

    private PlanetChunkOld findNeighboringChunk(PlanetChunkOld []arr, Vector3f v) {
        
        
        float test = 0.0001f;
        PlanetChunkOld chunk;
        
        for(PlanetChunkOld n : arr)
            for(int i=0;i<n.subChunks.size();i++){
                chunk = n.subChunks.get(i);
                for (Vector3f t : chunk.vertices) {
                    if(Math.abs(v.x-t.x) < test && Math.abs(v.y - t.y) < test && Math.abs(v.z - t.z)< test){
                        
                        if(chunk.depth != this.depth)
                            System.out.println("findNeighboringChunks STOP, different depths");
                        
                        return chunk;
                    }
                }
            }
        return null;
            
        
    }
    
     private Vector3f calculateNormal(int i){
        
        //source https://hub.jmonkeyengine.org/t/calculating-vertex-normals-of-custom-mesh/28179/4
        
         
         
        Vector3f v0 = new Vector3f(vertices[i]);
        Vector3f v1 = nodes[i][1];
        Vector3f v2 = nodes[i][2];
        Vector3f v3 = nodes[i][3];
        Vector3f v4 = nodes[i][0];
        Vector3f r1, r2, r3, r4, normal;
        
        
        
        
        int newIndex, nx,ny;
            int x = i % resolution;
            int y = i / resolution;
        
        
        int offset = 2;
        //west vertex
        if(x==resolution -1)
            v1 = new Vector3f(this.neighbors[0].vertices[i - resolution + offset]);
        else
            v1 = new Vector3f(vertices[i+1]);
        
        //south vertex
        if(y==resolution -1)
            v2 = new Vector3f(this.neighbors[1].vertices[i - ((resolution-offset)*resolution)]);
        else
            v2 = new Vector3f(vertices[i+resolution]);
        //east vertex
        if(x==0)
            v3 = new Vector3f(this.neighbors[2].vertices[i + resolution - offset]);
        else
            v3 = new Vector3f(vertices[i-1]);
        //north vertex
        if(y==0)
            v4 = new Vector3f(this.neighbors[3].vertices[i + ((resolution-offset)*resolution)]);
        else
            v4 = new Vector3f(vertices[i-resolution]);
        
        /*Vector3f v0 = new Vector3f(vertices[i]); // Will store the vert you are calculating for
        Vector3f v1 = new Vector3f(i + 1 < vertices.length -1 ? vertices[i+1]:                  this.neighbors[0].vertices[i - resolution + 2]); // 1st surrounding vert (in clockwise fashion)
        Vector3f v2 = new Vector3f(i + resolution < vertices.length ? vertices[i+resolution] :  this.neighbors[1].vertices[i - ((resolution-2)*resolution)]); // 2nd surrounding vert
        Vector3f v3 = new Vector3f(i > 0 ? vertices[i-1] :                                      this.neighbors[2].vertices[i + resolution - 2]); // 3rd surrounding vert
        Vector3f v4 = new Vector3f(i - resolution > -1 ? vertices[i-resolution]:                this.neighbors[3].vertices[i + ((resolution-2)*resolution)]); // 4th surrounding vert*/
        //Vector3f r1, r2, r3, r4, normal;

        // Next populate (.set) the temp verts
        // I'll leave this up to you

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
        
        
        return normal;
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
    
    public Vector3f setMidPoint(){
        return this.midPoint = vertices[(resolution*resolution)/2];
    }
    
    
   
    
    private Vector3f positionVertex(Vector3f _point, Sides side){
        
        Vector3f point = new Vector3f(_point);
        
        switch (side){
            
            case TOP: point = rotateVertex(_point,0,0);break;
            case BOTTOM: point = rotateVertex(_point, PI, 0); break;
            case LEFT: point = rotateVertex(_point,0,HALFPI); break;
            case RIGHT: point = rotateVertex(_point,0,THREEHPI); break;
            case INFRONT: point = rotateVertex(_point,HALFPI,0);  break;
            case BEHIND: point = rotateVertex(_point, THREEHPI,0); break;
        }
        
        if (this.planet.isSphere)
            point.normalizeLocal();
        //point.multLocal(this.scale);
        
        return point;
    }
    
    private void generateHeights(){
        for(int i=0; i<this.vertices.length;i++){
            //float localHeight = HeightGenerator.generateHeight(this.vertices[i]);
            float elevation = shapeGenerator.calculatePointOnPlanet(this.vertices[i]);
            
            this.vertices[i].multLocal(1+elevation).multLocal(this.planet.scale);
            this.heights[i] = elevation;
            
        }
    }
    
    private Vector3f rotateVertex(Vector3f _point, float XRotation, float ZRotation){
        
        
        
        Vector3f point = new Vector3f(_point).add(new Vector3f(-0.5f,0.5f,-0.5f));
        Vector3f bufPoint = new Vector3f(point);
        
              
        
        if ( XRotation > 0){
            //rotate on x axis
            point.x =  bufPoint.x; 
            point.y =  bufPoint.y * (float)Math.cos(XRotation) - bufPoint.z * (float)Math.sin(XRotation); // y cos - z sin
            point.z =  bufPoint.y * (float)Math.sin(XRotation) + bufPoint.z * (float)Math.cos(XRotation);  // y sin + z cos
        } 
        
        if (ZRotation > 0){
            //rotate on z axis
            point.x =  bufPoint.x * (float)Math.cos(ZRotation) - bufPoint.y * (float)Math.sin(ZRotation); //(x*cos(theta) - y sin(theta))
            point.y =  bufPoint.x * (float)Math.sin(ZRotation) + bufPoint.y * (float)Math.cos(ZRotation); //(x sin + y cos)
            point.z =  bufPoint.z;
        }
        
        
        return point;
        
    }

    

}
