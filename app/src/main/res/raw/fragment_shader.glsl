#version 300 es
//声明float型变量的精度为mediump
precision mediump float;
in vec4 vColor;
out vec4 fragColor;
void main() {
                fragColor = vColor;
            }