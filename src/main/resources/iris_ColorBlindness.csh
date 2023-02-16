#version 430 core
layout(local_size_x = 8, local_size_y = 8) in;

const vec2 workGroupsRender = vec2(1.0, 1.0);

#define NONE 0
#define PROTANOPIA 1
#define DEUTERANOPIA 2
#define TRITANOPIA 3

#define CURRENT_COLORBLIND_MODE PLACEHOLDER

//[PROTANOPIA DEUTERANOPIA TRITANOPIA]

layout(rgba8) uniform image2D mainImage;

void main() {
    #if CURRENT_COLORBLIND_MODE == NONE
    return;
    #else
    ivec2 PixelIndex = ivec2(gl_GlobalInvocationID.xy);
    vec4 tex = imageLoad(mainImage, PixelIndex);

    float L = (17.8824 * tex.r) + (43.5161 * tex.g) + (4.11935 * tex.b);
    float M = (3.45565 * tex.r) + (27.1554 * tex.g) + (3.86714 * tex.b);
    float S = (0.0299566 * tex.r) + (0.184309 * tex.g) + (1.46709 * tex.b);

    float l, m, s;
    #if CURRENT_COLORBLIND_MODE == PROTANOPIA
        l = 0.0 * L + 2.02344 * M + -2.52581 * S;
        m = 0.0 * L + 1.0 * M + 0.0 * S;
        s = 0.0 * L + 0.0 * M + 1.0 * S;
    #elif CURRENT_COLORBLIND_MODE == DEUTERANOPIA
    l = 1.0 * L + 0.0 * M + 0.0 * S;
        m = 0.494207 * L + 0.0 * M + 1.24827 * S;
        s = 0.0 * L + 0.0 * M + 1.0 * S;
    #elif CURRENT_COLORBLIND_MODE == TRITANOPIA
        l = 1.0 * L + 0.0 * M + 0.0 * S;
        m = 0.0 * L + 1.0 * M + 0.0 * S;
        s = -0.395913 * L + 0.801109 * M + 0.0 * S;
    #endif

    vec4 error;
    error.r = (0.0809444479 * l) + (-0.130504409 * m) + (0.116721066 * s);
    error.g = (-0.0102485335 * l) + (0.0540193266 * m) + (-0.113614708 * s);
    error.b = (-0.000365296938 * l) + (-0.00412161469 * m) + (0.693511405 * s);
    error.a = 1.0;
    vec4 diff = tex - error;
    vec4 correction;
    correction.r = 0.0;
    correction.g =  (diff.r * 0.7) + (diff.g * 1.0);
    correction.b =  (diff.r * 0.7) + (diff.b * 1.0);
    correction = tex + correction;
    correction.a = tex.a * INTENSITY;

    imageStore(mainImage, PixelIndex, vec4(mix(tex.rgb, correction.rgb, correction.a), tex.a));
    #endif
}
