#version 150
uniform sampler2D DiffuseSampler;
uniform vec2 ScreenSize;
uniform vec2 RectPos;
uniform vec2 RectSize;
uniform float Radius;
uniform float Smoothness;
uniform vec4 ColorModulator;
in vec2 texCoord;
out vec4 fragColor;

float roundedBoxSDF(vec2 position, vec2 halfSize, float radius) {
    vec2 q = abs(position) - halfSize + radius;
    return length(max(q, vec2(0.0))) + min(max(q.x, q.y), 0.0) - radius;
}

void main() {
    vec2 screenPixel = RectPos + texCoord * RectSize;
    vec2 screenUV = screenPixel / ScreenSize;
    vec4 source = texture(DiffuseSampler, vec2(screenUV.x, 1.0 - screenUV.y));

    vec2 position = (texCoord - 0.5) * RectSize;
    vec2 halfSize = RectSize * 0.5;
    float clampedRadius = min(Radius, min(halfSize.x, halfSize.y));
    float distance = roundedBoxSDF(position, halfSize, clampedRadius);

    float aa = max(fwidth(distance) * 1.5, Smoothness);
    float alpha = 1.0 - smoothstep(-aa, aa, distance);

    if (alpha <= 0.001) {
        discard;
    }

    fragColor = source * ColorModulator;
    fragColor.a *= alpha;
}