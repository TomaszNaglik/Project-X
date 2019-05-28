
uniform sampler2D m_tWater;

varying vec2 texCoord;


void main(){
gl_FragColor = texture2D(m_tWater,texCoord);
    //gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);
}
