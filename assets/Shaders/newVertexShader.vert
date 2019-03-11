uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform float g_Time;
attribute vec3 inPosition;


out vec4 position;


void main(){
    
    vec4 pos = vec4(inPosition, 1.0);
    pos.y = sin((inPosition.x + g_Time*10)/10)*2 + sin((inPosition.z + g_Time*10)/6)*4;
    position = g_WorldViewProjectionMatrix * pos;
    gl_Position = g_WorldViewProjectionMatrix * pos;

    //position = g_WorldMatrix * pos;
    
}
