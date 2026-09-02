#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float Radius;
uniform vec2 Direction;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / OutSize;
    float r = max(Radius, 1.0);

    vec4 color =
    texture(DiffuseSampler, texCoord)
    * 0.227027;

    vec2 off1 =
    Direction
    * texel
    * r
    * 1.384615;

    vec2 off2 =
    Direction
    * texel
    * r
    * 3.230769;

    color +=
    texture(
            DiffuseSampler,
            texCoord + off1
    ) * 0.316216;

    color +=
    texture(
            DiffuseSampler,
            texCoord - off1
    ) * 0.316216;

    color +=
    texture(
            DiffuseSampler,
            texCoord + off2
    ) * 0.070270;

    color +=
    texture(
            DiffuseSampler,
            texCoord - off2
    ) * 0.070270;

    fragColor = color;
}