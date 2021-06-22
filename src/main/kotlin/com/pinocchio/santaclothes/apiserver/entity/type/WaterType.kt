package com.pinocchio.santaclothes.apiserver.entity.type

enum class WaterType(var code: Int, var nationType: NationType, var description: String, var imageUrl:String) {
    HAND_KR(0, NationType.KR, "손 세탁 가능, 중성 세제", "/img/waterwash/handwash.png"), FORBIDDEN_KR(1, NationType.KR, "물세탁 불가", "/img/waterwash/nohandwash.png"), WASHER_30_NEUTRAL_KR(
        2,
        NationType.KR,
        "세탁기 약 30도 중성", "/img/waterwash/machine_30.png"
    ),
    WASHER_ABOUT_40_KR(3, NationType.KR, "세탁기 약 40도", "/img/waterwash/machine_40.png"), WASHER_40_KR(4, NationType.KR, "세탁기 40도", "/img/waterwash/machine40.png"), WASHER_60_KR(
        5,
        NationType.KR,
        "세탁기 60도", "/img/waterwash/machine60.png"
    ),
    WASHER_90_KR(6, NationType.KR, "세탁기 95도", "/img/waterwash/machine95.png"), HAND_USA(7, NationType.USA, "손 세탁 가능", "/img/waterwash/ame_handwash.png"), FORBIDDEN_USA(
        8,
        NationType.USA,
        "물 세탁 금지", "/img/waterwash/ame_no.png"
    ),
    WASHER_USA(9, NationType.USA, "기계 세탁 가능", "/img/waterwash/ame_mac.png"), FORBIDDEN_BLOWER_USA(10, NationType.USA, "비틀기 금지", "/img/waterwash/ame_nosqueeze.png"), HAND_30_JP(
        11,
        NationType.JP,
        "30도 손세탁 가능", "/img/waterwash/jap_30hand.png"
    ),
    WASHER_95_JP(12, NationType.JP, "세탁기 95도", "/img/waterwash/jap_95mac.png"), HAND_WEAK_JP(13, NationType.JP, "약한 손세탁", "/img/waterwash/jap_light.png"), FORBIDDEN_BLOWER_JP(
        14,
        NationType.JP,
        "비틀기 금지", "/img/waterwash/jap_nosqueeze.png"
    ),
    FORBIDDEN_JP(15, NationType.JP, "물세탁 불가", "/img/waterwash/jap_no.png");
}
