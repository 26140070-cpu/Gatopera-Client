#version 150

uniform vec2 Size;
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
    vec2 position = (texCoord - 0.5) * Size;
    vec2 halfSize = Size * 0.5;

    float distance = roundedBoxSDF(
            position,
            halfSize,
            min(Radius, min(halfSize.x, halfSize.y))
    );

    float alpha = 1.0 - smoothstep(
            0.0,
            max(Smoothness, 0.001),
            distance
    );

    if (alpha <= 0.0) {
        discard;
    }

    fragColor = ColorModulator * vec4(
            1.0,
            1.0,
            1.0,
            alpha
    );
}