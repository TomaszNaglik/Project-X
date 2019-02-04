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
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.util.TangentBinormalGenerator;
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
    
    private int resolutionX;
    private int resolutionY;
    
    private final int[] indices;
    private float [] colorArray;
   
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
        colorArray = new float[4*mapNodes.size()];
        
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
            //Triangle definition
            if (position.x != resolutionX - 1 && position.y != resolutionY - 1)
            {
                //if(isAntiClockwise){
                    indices[triIndex] = i;
                    indices[triIndex + 1] = i + resolutionX ;
                    indices[triIndex + 2] = i + resolutionX + 1;

                    indices[triIndex + 3] = i;
                    indices[triIndex + 4] = i + resolutionX + 1;
                    indices[triIndex + 5] = i  + 1;
                    
              /*  }else{
                    indices[triIndex] = i;
                    indices[triIndex + 1] = i + resolutionX + 1;
                    indices[triIndex + 2] = i + resolutionX;

                    indices[triIndex + 3] = i;
                    indices[triIndex + 4] = i + 1;
                    indices[triIndex + 5] = i + resolutionX + 1;
                }*/
                triIndex += 6;
            }
            
            //Color definition
            colorArray[colIndex++] = mapNodes.get(i).colors[0];
            colorArray[colIndex++] = mapNodes.get(i).colors[1];
            colorArray[colIndex++] = mapNodes.get(i).colors[2];
            colorArray[colIndex++] = mapNodes.get(i).colors[3];
        }
    }

    private void buildMesh() {
        mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, BufferUtils.createFloatBuffer(normals));
        mesh.updateBound();
        
        Geometry geo = new Geometry(this.name+" Geometry", mesh); // using our custom mesh object
        
         
        
        Material matLight = new Material(StaticAssets.assetManager, "Common/MatDefs/Light/Lighting.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md"
        
        mesh.setBuffer(VertexBuffer.Type.Color, 4, colorArray);
        if(!isAntiClockwise) matLight.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        matLight.getAdditionalRenderState().setWireframe(isWireframe);
        matLight.setBoolean("UseVertexColor", true);
        matLight.setBoolean("UseMaterialColors", true);
        matLight.setBoolean("VertexLighting", true);
        matLight.setColor("Ambient", ColorRGBA.White); //Using white here, but shouldn't matter that much
        matLight.setColor("Diffuse", ColorRGBA.White);
        matLight.setColor("Specular", ColorRGBA.White); //Using yellow for example
        matLight.setFloat("Shininess", 0);

        geo.setMaterial(matLight);
        this.attachChild(geo);
    }
    
}
