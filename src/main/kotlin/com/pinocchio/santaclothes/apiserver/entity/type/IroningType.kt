package com.pinocchio.santaclothes.apiserver.entity.type

enum class IroningType(var code: Int, var nationType: NationType, var description: String) {
    FORBIDDEN_KR(0, NationType.KR, "다림질 불가"), IRONING_80_120_KR(
        1,
        NationType.KR,
        "80~120도 가능"
    ),
    IRONING_80_120_FABRIC_KR(2, NationType.KR, "80~120도 천 깔고만 가능"), IRONING_140_160_KR(
        3,
        NationType.KR,
        "140~160도 가능"
    ),
    IRONING_140_160_FABRIC_KR(4, NationType.KR, "140~160도 천 깔고만 가능"), IRONING_180_210_KR(
        5,
        NationType.KR,
        "180~210도 가능"
    ),
    IRONING_180_210_FABRIC_KR(6, NationType.KR, "180~210도 천 깔고만 가능"), FORBIDDEN_USA(
        7,
        NationType.USA,
        "다림질 불가"
    ),
    FORBIDDEN_STEAM_USA(8, NationType.USA, "스팀 다리미 불가"), STEAM_USA(9, NationType.USA, "스팀 다리미 가능"), FORBIDDEN_JP(
        10,
        NationType.JP,
        "다림질 불가"
    ),
    IRONING_80_120_JP(11, NationType.JP, "80~120도"), IRONING_140_160_JP(
        12,
        NationType.JP,
        "140~160도"
    ),
    IRONING_180_210_JP(13, NationType.JP, "180~210도");
}
