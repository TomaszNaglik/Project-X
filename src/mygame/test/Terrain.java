/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.test;

import com.jme3.asset.AssetManager;
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
import com.jme3.util.TangentBinormalGenerator;
import core.Noise.BiomeMap.BiomeGenerator;
import core.Noise.BiomeMap.BiomeSettings;
import core.Noise.HeightMap.ShapeGenerator;
import core.Noise.HeightMap.ShapeSettings;

/**
 *
 * @author Tomasz.Naglik
 */
public class Terrain extends Node{
    
    private float lifetime = 0;
    
    private final AssetManager assetManager;
    private static final  ShapeSettings SHAPE_SETTINGS = new ShapeSettings();
    private static final ShapeGenerator SHAPE_GENERATOR = new ShapeGenerator(SHAPE_SETTINGS);
    private static final BiomeSettings BIOME_SETTINGS = new BiomeSettings();
    
    private Mesh mesh = new Mesh();
    private Geometry geo;
    
    
    private int resolution = 550;
    private int tresolution = 10;
    private Vector3f [] vertices = new Vector3f[resolution*resolution];
    private Vector2f [] textCoords = new Vector2f[resolution * resolution];
    private Vector3f []  normals = new Vector3f[resolution*resolution];
    private int [] indices = new int[6*(resolution-1)*(resolution-1)];
    private float [] colorArray = new float[4*resolution];
    private Vector4f [] biome4 = new Vector4f[resolution*resolution];
    private Vector2f [] biome2t = new Vector2f[resolution*resolution];
    private Vector2f [] biome3t = new Vector2f[resolution*resolution];
    private Vector2f [] biome4t = new Vector2f[resolution*resolution];
    private Vector2f [] biome5t = new Vector2f[resolution*resolution];
    private Vector2f [] biome6t = new Vector2f[resolution*resolution];
    private Vector2f [] biome7t = new Vector2f[resolution*resolution];
    private Vector2f [] biome8t = new Vector2f[resolution*resolution];
    
    private float[] biomesSet = new float[18];
    public Terrain(AssetManager assetManager){
        
        this.assetManager = assetManager;
        mesh = createMesh();
    }
    
    
    public final Mesh createMesh(){
        for(int x = 0 ; x < resolution ; x++){
            for(int y = 0 ; y < resolution ; y++){
                float h = SHAPE_GENERATOR.calculatePointOnPlanet(new Vector3f(x,0,y))*300;
                vertices[x+(y*resolution)] = new Vector3f(x-resolution/2,h,y-resolution/2);
                textCoords[x+(y*resolution)] = new Vector2f(tresolution*(float)x/resolution,tresolution*(float)y/resolution);
                normals[x+(y*resolution)] = new Vector3f(0,1,0);
                
                biomesSet = BiomeGenerator.generateBiomeSet(vertices[x+(y*resolution)]);
                
                biome4[x+(y*resolution)].x = biomesSet[0];
                biome4[x+(y*resolution)].y = biomesSet[1];
                biome4[x+(y*resolution)].z = biomesSet[2];
                biome4[x+(y*resolution)].w = biomesSet[3];
                
                biome2t[x+(y*resolution)].x = biomesSet[4];
                biome2t[x+(y*resolution)].y = biomesSet[5];
                
                biome3t[x+(y*resolution)].x = biomesSet[6];
                biome3t[x+(y*resolution)].y = biomesSet[7];
                
                biome4t[x+(y*resolution)].x = biomesSet[8];
                biome4t[x+(y*resolution)].y = biomesSet[9];
                
                biome5t[x+(y*resolution)].x = biomesSet[10];
                biome5t[x+(y*resolution)].y = biomesSet[11];
                
                biome6t[x+(y*resolution)].x = biomesSet[12];
                biome6t[x+(y*resolution)].y = biomesSet[13];
                
                biome7t[x+(y*resolution)].x = biomesSet[14];
                biome7t[x+(y*resolution)].y = biomesSet[15];
                
                biome8t[x+(y*resolution)].x = biomesSet[16];
                biome8t[x+(y*resolution)].y = biomesSet[17];
                
            }
        }
        
        int triIndex = 0;
        for(int x = 0 ; x < resolution-1 ; x++){
            for(int y = 0 ; y < resolution-1 ; y++){
                int baseIndex = x+y*resolution;
                indices[triIndex++] = baseIndex;
                indices[triIndex++] = baseIndex + resolution + 1;
                indices[triIndex++] = baseIndex + resolution;
                
                indices[triIndex++] = baseIndex + 1;
                indices[triIndex++] = baseIndex + resolution + 1;
                indices[triIndex++] = baseIndex;
            }   
        }
        
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.TexCoord,   2, BufferUtils.createFloatBuffer(textCoords));
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(biome4));
        
        mesh.updateBound();
        
        
        
        geo = new Geometry(this.name+" Geometry", mesh); // using our custom mesh object
        
         
        
        Material matLight = new Material(assetManager, "MatDefs/textureMatDef.j3md"); //"Common/MatDefs/Light/Lighting.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md"
        Texture water = assetManager.loadTexture("Textures/water.jpg");
        Texture t1 = assetManager.loadTexture("Textures/t1.png");
        Texture t2 = assetManager.loadTexture("Textures/t2.png");
        water.setWrap(Texture.WrapMode.Repeat);
        t1.setWrap(Texture.WrapMode.Repeat);
        t2.setWrap(Texture.WrapMode.Repeat);
        matLight.setTexture("DiffuseMap", water);
        matLight.setTexture("T1", t1);
        matLight.setTexture("T2", t2);
        //mesh.setBuffer(VertexBuffer.Type.Color, 4, colorArray);
        matLight.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        matLight.getAdditionalRenderState().setWireframe(false);
        //matLight.setBoolean("UseVertexColor", true);
        
        //matLight.setBoolean("UseMaterialColors", true);
        //matLight.setBoolean("VertexLighting", true);
        matLight.setColor("Ambient", ColorRGBA.White); //Using white here, but shouldn't matter that much
        matLight.setColor("Diffuse", ColorRGBA.White);
        matLight.setColor("Specular", ColorRGBA.White); //Using yellow for example
        matLight.setFloat("Shininess", 1);
        

        geo.setMaterial(matLight);
        this.attachChild(geo);
        
        
        return mesh;
    }
    
    
    public final void updateMesh(float delta){
        float speed = 10;
        float spread = 10;
        lifetime += delta;
        lifetime %= (Math.PI*2);
        for(int i=0; i< resolution ; i++){
            for(int j=0; j< resolution ; j++){
                vertices[i+j*resolution].y = (float)Math.sin((j+i+lifetime*speed)/spread) + (float)Math.sin((j+i+lifetime*speed*0.2))/3;
            }
        }
       
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.updateBound();
        TangentBinormalGenerator.generate(mesh);
        
        
        
    }
}
