package com.pinocchio.santaclothes.apiserver.entity.type

enum class WaterType(var code: Int, var nationType: NationType, var description: String) {
    HAND_KR(0, NationType.KR, "손 세탁 가능, 중성 세제"), FORBIDDEN_KR(1, NationType.KR, "물세탁 불가"), WASHER_30_NEUTRAL_KR(
        2,
        NationType.KR,
        "세탁기 약 30도 중성"
    ),
    WASHER_ABOUT_40_KR(3, NationType.KR, "세탁기 약 40도"), WASHER_40_KR(4, NationType.KR, "세탁기 40도"), WASHER_60_KR(
        5,
        NationType.KR,
        "세탁기 60도"
    ),
    WASHER_90_KR(6, NationType.KR, "세탁기 95도"), HAND_USA(7, NationType.USA, "손 세탁 가능"), FORBIDDEN_USA(
        8,
        NationType.USA,
        "물 세탁 금지"
    ),
    WASHER_USA(9, NationType.USA, "기계 세탁 가능"), FORBIDDEN_BLOWER_USA(10, NationType.USA, "비틀기 금지"), HAND_30_JP(
        11,
        NationType.JP,
        "30도 손세탁 가능"
    ),
    WASHER_95_JP(12, NationType.JP, "세탁기 95도"), HAND_WEAK_JP(13, NationType.JP, "약한 손세탁"), FORBIDDEN_BLOWER_JP(
        14,
        NationType.JP,
        "비틀기 금지"
    ),
    FORBIDDEN_JP(15, NationType.JP, "물세탁 불가");
}
