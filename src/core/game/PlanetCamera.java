/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.game;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import core.Planet.Earth;
import core.Statics.StaticAssets;

/**
 *
 * @author Tomasz.Naglik
 */
public class PlanetCamera {
    
    Earth planet;
    Camera camera;
    Vector3f newCamPos, newCamDir;
    
        
    float angularAcceleration ;
    float baseAngularAcceleration = 10f;
    float zoomAcceleration =1;
    float zoomVel = 0;
    float friction = 0.98f;
        
    float zoom ;
    float minZoom ;
    float maxZoom ;
    
    float theta= 2.3785641f, rho = 2.220182f;//(float)Math.PI/2;; // theta ZY(0-360 ), rho XY (0-180)
    float rhoDir = 2*(float)Math.PI/3; 
    float directionOffset = 0;
    float baseDirectionOffset = (float)Math.PI/20;
    
    float velTheta = 0, velRho = 0;
    
    float maxLat = 0.98f * (float)Math.PI/2;
    
    
    public PlanetCamera(Earth planet){
        this.planet = planet;
        this.camera = StaticAssets.camera;
        this.newCamPos = new Vector3f(camera.getLocation());
        this.newCamDir = new Vector3f(camera.getDirection());
        
       
        minZoom = (planet.SCALE + camera.getFrustumNear())*1.05f;
        maxZoom = (planet.SCALE + camera.getFrustumNear())*5;
        zoom = planet.SCALE * 1.3f;
    }
    
    public void update(float tpf){
        calculateNewCameraPosition(tpf);
        calculateNewCameraDirection();
    }
    
    public void move(Direction direction, float tpf, float multiplier){
        
        switch (direction){
           
            case UP:        velRho -= (angularAcceleration * tpf * multiplier);
                            break;
            case DOWN:      velRho += (angularAcceleration * tpf* multiplier);
                            break;
            case LEFT:      velTheta -= (angularAcceleration * tpf* multiplier);
                            break;
            case RIGHT:     velTheta += (angularAcceleration * tpf* multiplier);
                            break;
            case FORWARD:   zoomVel -= (zoomAcceleration * tpf* multiplier);
                            
                            break;
            case BACKWARD:  zoomVel += (zoomAcceleration * tpf* multiplier);
                            break;
            
            
        }
    }
    
    private void validatePosition(float tpf){
        //calc theta as angle to XZ plane. Make sure it is between +80 and -80 deg
        
        
        if(rho>maxLat*2){
            rho = maxLat*2;
            velRho = 0;
        }
        if(rho< Math.PI/2-maxLat){
            rho = (float)Math.PI/2-maxLat;
            velRho = 0;
        }
        
        rhoDir +=(velRho *tpf);
        
        if(zoom > maxZoom)
            zoom = maxZoom;
        if(zoom < minZoom)
            zoom = minZoom;
        
        
    }
    
    private void calculateNewCameraPosition(float tpf){
        
        theta += (velTheta *tpf );
        rho += (velRho*tpf) ;
        zoom +=(zoomVel );
        directionOffset = (baseDirectionOffset * ((zoom-planet.SCALE)/ (maxZoom - planet.SCALE)))*6f;
        angularAcceleration = baseAngularAcceleration *((zoom-planet.SCALE)/ (maxZoom - planet.SCALE)) *2;
        
        validatePosition(tpf);
        
        newCamPos.z = zoom * (float)Math.sin(rho  ) * (float)Math.cos(theta);
        newCamPos.x = zoom * (float)Math.sin(rho  ) * (float)Math.sin(theta);
        newCamPos.y = zoom * (float)Math.cos(rho  );
        camera.setLocation(newCamPos);
        camera.setFrustumFar(3*(zoom-planet.SCALE));
        
        dampenVelocity();
    }

    private void dampenVelocity() {
        velTheta *= friction;
        velRho *= friction;
        zoomVel *= friction;
    }

    private void calculateNewCameraDirection() {
        
        newCamDir.z = planet.SCALE * (float)Math.sin(rhoDir - directionOffset ) * (float)Math.cos(theta);
        newCamDir.x = planet.SCALE * (float)Math.sin(rhoDir - directionOffset ) * (float)Math.sin(theta);
        newCamDir.y = planet.SCALE * (float)Math.cos(rhoDir - directionOffset );
                
        camera.lookAt(newCamDir, newCamPos);
    }

}

