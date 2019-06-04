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
    
    boolean isWireframe = false;
    
    
    public final ArrayList<MapNode> mapNodes = new ArrayList<>();
    public final Vector3f[] vertices;
    private final Vector3f[] normals;
    private final int[] indices;
    private Vector2f [] texCoords;
    private Vector4f [] biome1t;
    private Vector4f [] biome2t;
    private Vector4f [] biome3t;
        
    private final int resolutionX;
    private final int resolutionY;
    private boolean isAntiClockwise;
    private Mesh mesh;
    public Vector3f center;
    
    public static Texture Ocean = StaticAssets.assetManager.loadTexture("Textures/Ocean.jpg");
    public static Texture Sea = StaticAssets.assetManager.loadTexture("Textures/Sea.jpg");
    public static Texture Beach = StaticAssets.assetManager.loadTexture("Textures/Beach.jpg");
    public static Texture Grassland = StaticAssets.assetManager.loadTexture("Textures/Grassland.jpg");
    public static Texture Plains = StaticAssets.assetManager.loadTexture("Textures/Plains.jpg");
    public static Texture Cliffs = StaticAssets.assetManager.loadTexture("Textures/Cliffs.jpg");
    public static Texture Glacier = StaticAssets.assetManager.loadTexture("Textures/Glacier.jpg");
    public static Texture Snow_Peaks = StaticAssets.assetManager.loadTexture("Textures/Snow Peaks.jpg");
    public static Texture Desert = StaticAssets.assetManager.loadTexture("Textures/Desert.jpg");
    public static Texture River = StaticAssets.assetManager.loadTexture("Textures/River.jpg");
    
    public PlanetFace(ArrayList<MapNode> array, int xResolution, int yResolution, boolean isAntiClockwise) {
        
        this.isAntiClockwise = isAntiClockwise;
        this.resolutionX = xResolution;
        this.resolutionY = yResolution;
        
        for(int i=0; i< array.size(); i++)
            mapNodes.add(array.get(i));
        
        vertices = new Vector3f[mapNodes.size()];
        normals = new Vector3f[mapNodes.size()];
        indices = new int[6*(resolutionX-1)*(resolutionY-1)];
        
        biome1t = new Vector4f[mapNodes.size()];
        biome2t = new Vector4f[mapNodes.size()];
        biome3t = new Vector4f[mapNodes.size()];
        
        
        texCoords = new Vector2f[mapNodes.size()];
        
        setupMesh();
        buildMesh();
        
        center = vertices[vertices.length/2];
        
    }

    private void setupMesh() {
        int triIndex = 0;
        for (int i = 0 ; i < mapNodes.size() ; i++){
            vertices[i] = mapNodes.get(i).vertex;
            
            if(isAntiClockwise)
                normals[i] = mapNodes.get(i).normal;
            else
                normals[i] = mapNodes.get(i).normal.mult(1);
            
            Vector2f position = new Vector2f((int)i % (resolutionX),(int)i / (resolutionY));
            
            float[] biomesSet = BiomeGenerator.generateBiomeSet(mapNodes.get(i));
           
              
                biome1t[i] = new Vector4f();
                biome1t[i].x = biomesSet[0];
                biome1t[i].y = biomesSet[1];
                biome1t[i].z = biomesSet[2];
                biome1t[i].w = biomesSet[3];
                
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
        }
    }

    private void buildMesh() {
        mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(biome1t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
        mesh.setBuffer(VertexBuffer.Type.TexCoord2, 4, BufferUtils.createFloatBuffer(biome2t));
        mesh.setBuffer(VertexBuffer.Type.TexCoord3, 4, BufferUtils.createFloatBuffer(biome3t));
        
        mesh.updateBound();
        
        Geometry geo = new Geometry(this.name+" Geometry", mesh); // using our custom mesh object
        Material matLight = new Material(StaticAssets.assetManager, "MatDefs/textureMatDef.j3md"); //"Common/MatDefs/Light/Lighting.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md"
        
        
        
        
        
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

    boolean isInVisibleRange() {
        return StaticAssets.camera.getLocation().distance(center) < StaticAssets.viewThreshold;
    }
    
}
