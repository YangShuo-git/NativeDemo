// 把顶点坐标给这个变量（自己定义的），确定要画画的形状;  4个数组矩阵
attribute vec4 vPosition;
// 把纹理坐标给这个变量（自己定义的），接收采样器采样图片的坐标
attribute vec4 vCoord;

// 变换矩阵，需要将原本的vCoord 与矩阵相乘，才能得到 surfacetexure 的正确的采样坐标
uniform mat4 vMatrix;
//传给片元着色器 像素点
varying vec2 aCoord;

void main(){
    // 将vPosition传给gl_Position（内置变量），通知gpu需要渲染成什么样的形状
    gl_Position=vPosition;
    // 必须是矩阵*纹理坐标
    aCoord= (vMatrix * vCoord).xy;
}
