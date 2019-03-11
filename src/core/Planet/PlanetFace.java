/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Planet;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import core.Noise.BiomeMap.BiomeGenerator;
import core.Statics.StaticAssets;
import java.util.ArrayList;

/**
 *
 * @author Tomasz.Naglik
 */
public class PlanetFace extends Node{

    public final ArrayList<MapNode> mapNodes = new ArrayList<>();
    public final Vector3f[] vertices;
    private final Vector3f[] normals;
    
    private float [] colorArray = new float[mapNodes.size()];
        private Vector4f [] biome4;
        private Vector4f [] biome2t;
        private Vector4f [] biome3t;
        private Vector2f [] biome4t;
        private Vector2f [] biome5t;
        private Vector2f [] biome6t;
        private Vector2f [] biome7t;
        private Vector2f [] biome8t;
        
        private Vector2f [] texCoords;
    
        private float[] biomesSet;
    private int resolutionX;
    private int resolutionY;
    
    private final int[] indices;
    //private float [] colorArray;
    
    
   
    boolean isAntiClockwise;
    boolean isWireframe = false;
    private int xIndex;
    private int split;
    
    private Mesh mesh;
    
    public PlanetFace(ArrayList<MapNode> array, int xResolution, int yResolution, boolean isAntiClockwise) {
        
        this.isAntiClockwise = isAntiClockwise;
        int step =1;
        this.xIndex = 0;
        this.split = 0;
        
        this.resolutionX = xResolution;
        this.resolutionY = yResolution;
        
        
        
        for(int i=0; i< array.size(); i+=step)
            mapNodes.add(array.get(i));
        
        vertices = new Vector3f[mapNodes.size()];
        normals = new Vector3f[mapNodes.size()];
        indices = new int[6*(resolutionX-1)*(resolutionY-1)];
        
        biome4 = new Vector4f[mapNodes.size()];
        biome2t = new Vector4f[mapNodes.size()];
        biome3t = new Vector4f[mapNodes.size()];
        biome4t = new Vector2f[mapNodes.size()];
        biome5t = new Vector2f[mapNodes.size()];
        biome6t = new Vector2f[mapNodes.size()];
        biome7t = new Vector2f[mapNodes.size()];
        biome8t = new Vector2f[mapNodes.size()];
        
        texCoords = new Vector2f[mapNodes.size()];
        
        setupMesh();
        buildMesh();
        
        
        
    }

    private void setupMesh() {
        int triIndex = 0;
        int colIndex = 0;
        for (int i = 0 ; i < mapNodes.size() ; i++){
            vertices[i] = mapNodes.get(i).vertex;
            
            if(isAntiClockwise)
                normals[i] = mapNodes.get(i).normal;
            else
                normals[i] = mapNodes.get(i).normal.mult(1);
            
            Vector2f position = new Vector2f((int)i % (resolutionX),(int)i / (resolutionY));
            
            biomesSet = BiomeGenerator.generateBiomeSet(mapNodes.get(i));
           
              
                biome4[i] = new Vector4f();
                biome4[i].x = biomesSet[0];
                biome4[i].y = biomesSet[1];
                biome4[i].z = biomesSet[2];
                biome4[i].w = biomesSet[3];
                
                biome2t[i] = new Vector4f();
                biome2t[i].x = biomesSet[4];
                biome2t[i].y = biomesSet[5];
                biome2t[i].z = biomesSet[6];
                biome2t[i].w = biomesSet[7];
                
                biome3t[i] = new Vector4f();
                biome3t[i].x = biomesSet[8];
                biome3t[i].y = biomesSet[9];
                biome3t[i].z = biomesSet[10];
                biome3t[i].w = biomesSet[11];
                
                /*biome4t[i] = new Vector2f();
                biome4t[i].x = biomesSet[8];
                biome4t[i].y = biomesSet[9];
                
                biome5t[i] = new Vector2f();
                biome5t[i].x = biomesSet[10];
                biome5t[i].y = biomesSet[11];
                
                biome6t[i] = new Vector2f();
                biome6t[i].x = biomesSet[12];
                biome6t[i].y = biomesSet[13];
                
                biome7t[i] = new Vector2f();
                biome7t[i].x = biomesSet[14];
                biome7t[i].y = biomesSet[15];
                
                biome8t[i] = new Vector2f();
                biome8t[i].x = biomesSet[16];
                biome8t[i].y = biomesSet[17];*/
                
                int x = i % resolutionX;
                int y = i / resolutionY;
                int factor = 50;
                texCoords[i] = new Vector2f(factor*(float)x/resolutionX,factor*(float)y/resolutionY);



            //Triangle definition
            if (position.x != resolutionX - 1 && position.y != resolutionY - 1)
            {
                indices[triIndex] = i;
                indices[triIndex + 1] = i + resolutionX ;
                indices[triIndex + 2] = i + resolutionX + 1;

                indices[triIndex + 3] = i;
                indices[triIndex + 4] = i + resolutionX + 1;
                indices[triIndex + 5] = i  + 1;
                    
                triIndex += 6;
            }
            
            //Color definition
            //colorArray[colIndex++] = mapNodes.get(i).colors[0];
            //colorArray[colIndex++] = mapNodes.get(i).colors[1];
            //colorArray[colIndex++] = mapNodes.get(i).colors[2];
            //colorArray[colIndex++] = mapNodes.get(i).colors[3];
        }
    }

    private void buildMesh() {
        mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(biome4));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
        mesh.setBuffer(VertexBuffer.Type.TexCoord2, 4, BufferUtils.createFloatBuffer(biome2t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord3, 4, BufferUtils.createFloatBuffer(biome3t));
        /*mesh.setBuffer(VertexBuffer.Type.TexCoord4, 2, BufferUtils.createFloatBuffer(biome4t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord5, 2, BufferUtils.createFloatBuffer(biome5t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord6, 2, BufferUtils.createFloatBuffer(biome6t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord7, 2, BufferUtils.createFloatBuffer(biome7t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord8, 2, BufferUtils.createFloatBuffer(biome8t));*/
        mesh.updateBound();
        
        Geometry geo = new Geometry(this.name+" Geometry", mesh); // using our custom mesh object
         Material matLight = new Material(StaticAssets.assetManager, "MatDefs/textureMatDef.j3md"); //"Common/MatDefs/Light/Lighting.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md"
        //Texture water = StaticAssets.assetManager.loadTexture("Textures/water.jpg");
        //Texture t1 = StaticAssets.assetManager.loadTexture("Textures/t1.png");
        //Texture t2 = StaticAssets.assetManager.loadTexture("Textures/t2.png");
        
        Texture Ocean = StaticAssets.assetManager.loadTexture("Textures/Ocean.jpg");
        Texture Sea = StaticAssets.assetManager.loadTexture("Textures/Sea.jpg");
        Texture Beach = StaticAssets.assetManager.loadTexture("Textures/Beach.jpg");
        Texture Grassland = StaticAssets.assetManager.loadTexture("Textures/Grassland.jpg");
        Texture Plains = StaticAssets.assetManager.loadTexture("Textures/Plains.jpg");
        Texture Cliffs = StaticAssets.assetManager.loadTexture("Textures/Cliffs.jpg");
        Texture Glacier = StaticAssets.assetManager.loadTexture("Textures/Glacier.jpg");
        Texture Snow_Peaks = StaticAssets.assetManager.loadTexture("Textures/Snow Peaks.jpg");
        Texture Desert = StaticAssets.assetManager.loadTexture("Textures/Desert.jpg");
        Texture River = StaticAssets.assetManager.loadTexture("Textures/Ocean.jpg");
        
        
        //water.setWrap(Texture.WrapMode.Repeat);
        Ocean.setWrap(Texture.WrapMode.Repeat);
        Sea.setWrap(Texture.WrapMode.Repeat);
        Beach.setWrap(Texture.WrapMode.Repeat);
        Grassland.setWrap(Texture.WrapMode.Repeat);
        Plains.setWrap(Texture.WrapMode.Repeat);
        Cliffs.setWrap(Texture.WrapMode.Repeat);
        Glacier.setWrap(Texture.WrapMode.Repeat);
        Snow_Peaks.setWrap(Texture.WrapMode.Repeat);
        Desert.setWrap(Texture.WrapMode.Repeat);
        
        
        //matLight.setTexture("DiffuseMap", water);
        matLight.setTexture("tOcean", Ocean);
        matLight.setTexture("tSea", Sea);
        matLight.setTexture("tBeach", Beach);
        matLight.setTexture("tGrassland", Grassland);
        matLight.setTexture("tPlains", Plains);
        matLight.setTexture("tCliffs", Cliffs);
        matLight.setTexture("tGlacier", Glacier);
        matLight.setTexture("tSnow_Peaks", Snow_Peaks);
        matLight.setTexture("tDesert", Desert);
        matLight.setTexture("tRiver", River);
        
        //matLight.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        
        //mesh.setBuffer(VertexBuffer.Type.Color, 4, colorArray);
        if(!isAntiClockwise) matLight.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        matLight.getAdditionalRenderState().setWireframe(isWireframe);
        //matLight.setBoolean("UseVertexColor", true);
        
        //matLight.setBoolean("UseMaterialColors", true);
        //matLight.setBoolean("VertexLighting", true);
        matLight.setColor("Ambient", ColorRGBA.White); //Using white here, but shouldn't matter that much
        matLight.setColor("Diffuse", ColorRGBA.White);
        matLight.setColor("Specular", ColorRGBA.White); //Using yellow for example
        matLight.setFloat("Shininess", 50);
        //matLight.setBoolean("UseReflection", true);

        geo.setMaterial(matLight);
        this.attachChild(geo);
    }
    
}
