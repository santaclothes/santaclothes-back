package com.pinocchio.santaclothes.apiserver.entity.type

enum class WaterType(var code: Int, var nationType: NationType, var description: String, var imageUrl:String) {
    HAND_KR(0, NationType.KR, "물 온도 30°C로 중성세제를 사용하여 손세탁하세요.", "/img/waterwash/handwash.png"), FORBIDDEN_KR(1, NationType.KR, "물세탁이 불가합니다", "/img/waterwash/nohandwash.png"), WASHER_30_NEUTRAL_KR(
        2,
        NationType.KR,
        "물 온도 30°C로 중성세제를 사용하여 세탁기로 약하게 세탁하세요", "/img/waterwash/machine_30.png"
    ),
    WASHER_ABOUT_40_KR(3, NationType.KR, "물 온도 40°C로 세탁기를 사용하여 약하게 세탁하세요", "/img/waterwash/machine_40.png"), WASHER_40_KR(4, NationType.KR, "물 온도 40°C로 세탁기를 사용하여 세탁하세요", "/img/waterwash/machine40.png"), WASHER_60_KR(
        5,
        NationType.KR,
        "물 온도 60°C로 세탁기를 사용하여 세탁하세요", "/img/waterwash/machine60.png"
    ),
    WASHER_90_KR(6, NationType.KR, "물 온도 95°C로 세탁기를 사용하여 세탁하세요(삶기 가능)", "/img/waterwash/machine95.png"), HAND_USA(7, NationType.USA, "손세탁하세요", "/img/waterwash/ame_handwash.png"), FORBIDDEN_USA(
        8,
        NationType.USA,
        "물 세탁이 불가합니다", "/img/waterwash/ame_no.png"
    ),
    WASHER_USA(9, NationType.USA, "보통세기로 기계세탁하세요", "/img/waterwash/ame_mac.png"), FORBIDDEN_BLOWER_USA(10, NationType.USA, "비틀기가 불가합니다", "/img/waterwash/ame_nosqueeze.png"), HAND_30_JP(
        11,
        NationType.JP,
        "물 온도 30°C로 세탁기를 사용하여 세탁하세요", "/img/waterwash/jap_30hand.png"
    ),
    WASHER_95_JP(12, NationType.JP, "물 온도 95°C로 세탁기를 사용하여 세탁하세요", "/img/waterwash/jap_95mac.png"), HAND_WEAK_JP(13, NationType.JP, "약하게 손세탁하세요", "/img/waterwash/jap_light.png"), FORBIDDEN_BLOWER_JP(
        14,
        NationType.JP,
        "비틀기가 불가합니다", "/img/waterwash/jap_nosqueeze.png"
    ),
    FORBIDDEN_JP(15, NationType.JP, "물세탁이 불가합니다", "/img/waterwash/jap_no.png");
}
