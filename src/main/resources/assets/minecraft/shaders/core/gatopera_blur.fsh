#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MaskSampler;

uniform vec2 ScreenSize;
uniform vec2 RectPos;
uniform vec2 RectSize;
uniform float Radius;
uniform float Smoothness;
uniform vec4 ColorModulator;

in vec2 texCoord;
out vec4 fragColor;

float roundedBoxSDF(
        vec2 position,
        vec2 halfSize,
        float radius
) {
    vec2 q =
    abs(position)
    - halfSize
    + radius;

    return length(max(q, vec2(0.0)))
    + min(max(q.x, q.y), 0.0)
    - radius;
}

void main() {
    vec2 screenPixel =
    RectPos
    + texCoord * RectSize;

    vec2 screenUV =
    screenPixel / ScreenSize;

    vec4 source =
    texture(
            DiffuseSampler,
            vec2(
                    screenUV.x,
                    1.0 - screenUV.y
            )
    );

    vec4 mask =
    texture(
            MaskSampler,
            texCoord
    );

    vec2 position =
    (texCoord - 0.5) * RectSize;

    vec2 halfSize =
    RectSize * 0.5;

    float distance =
    roundedBoxSDF(
            position,
            halfSize,
            min(
                    Radius,
                    min(
                            halfSize.x,
                            halfSize.y
                    )
            )
    );

    float alpha =
    1.0 - smoothstep(
            0.0,
            max(
                    Smoothness,
                    0.001
            ),
            distance
    );

    alpha *= mask.a;

    if (alpha <= 0.0) {
        discard;
    }

    fragColor =
    source
    * ColorModulator;

    fragColor.a *= alpha;
}