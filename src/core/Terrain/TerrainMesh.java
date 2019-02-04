/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.Terrain;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

/**
 *
 * @author Tomasz.Naglik
 */
public class TerrainMesh {
    
    private int width, height;
    private int size;
    private float scale;
    private Mesh mesh = new Mesh();
    private Vector3f[] vertices;
    private Vector2f[] textCoords;
    private Vector3f[] indices;
    private Vector3f[] normals;
    
    public TerrainMesh(int w, int h, float s){
        this.scale = s;
        this.width = w;
        this.height = h;
        this.size = w*h;
        vertices = new Vector3f[size];
        textCoords = new Vector2f[size];
        indices = new Vector3f[(w-1)*(h-1)*2*3];
        
        generateVertices();
        generateTextCoords();
        generateIndices();
        
        
    }
    
    private void generateVertices(){
        for(int z=0; z<height; z++){
            for(int x = 0; x< width; x++){
                int index = x + (z*height);
                Vector3f newVertex = new Vector3f(x*scale,0,z*scale);
                vertices[index] = newVertex;
            }
        }
    }
    private void generateTextCoords(){
        
    }
    private void generateIndices(){
        
    }
    
    
    
    
    
    
}
