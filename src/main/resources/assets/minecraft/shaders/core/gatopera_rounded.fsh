#version 150

uniform vec2 RectSize;
uniform float Radius;
uniform float Smoothness;
uniform vec4 ColorModulator;

in vec2 texCoord;
out vec4 fragColor;

float roundedBoxSDF(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return length(max(q, 0.0))
    + min(max(q.x, q.y), 0.0)
    - r;
}

void main() {
    vec2 size = RectSize;
    vec2 position = texCoord * size;
    vec2 center = size * 0.5;
    vec2 local = position - center;
    vec2 halfSize = size * 0.5;

    float distance = roundedBoxSDF(
            local,
            halfSize,
            Radius
    );

    float edge = max(
            Smoothness,
            0.01
    );

    float alpha = 1.0 - smoothstep(
            0.0,
            edge,
            distance
    );

    fragColor = vec4(
            ColorModulator.rgb,
            ColorModulator.a * alpha
    );
}