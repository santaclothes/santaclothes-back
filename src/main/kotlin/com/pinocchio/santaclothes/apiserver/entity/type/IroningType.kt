package com.pinocchio.santaclothes.apiserver.entity.type

enum class IroningType(var code: Int, var nationType: NationType, var description: String, var imageUrl:String) {
    FORBIDDEN_KR(0, NationType.KR, "다림질 불가", "/img/ironing/no.png"), IRONING_80_120_KR(
        1,
        NationType.KR,
        "80~120도 가능", "/img/ironing/to80120.png"
    ),
    IRONING_80_120_FABRIC_KR(2, NationType.KR, "80~120도 천 깔고만 가능", "/img/ironing/withcloth80120.png"), IRONING_140_160_KR(
        3,
        NationType.KR,
        "140~160도 가능", "/img/ironing/to140160.png"
    ),
    IRONING_140_160_FABRIC_KR(4, NationType.KR, "140~160도 천 깔고만 가능", "/img/ironing/withcloth140160.png"), IRONING_180_210_KR(
        5,
        NationType.KR,
        "180~210도 가능", "/img/ironing/180210.png"
    ),
    IRONING_180_210_FABRIC_KR(6, NationType.KR, "180~210도 천 깔고만 가능", "/img/ironing/withcloth180210.png"), FORBIDDEN_USA(
        7,
        NationType.USA,
        "다림질 불가", "/img/ironing/ame_no.png"
    ),
    FORBIDDEN_STEAM_USA(8, NationType.USA, "스팀 다리미 불가", "/img/ironing/ame_steamno.png"), STEAM_USA(9, NationType.USA, "스팀 다리미 가능", "/img/ironing/ame_steam.png"), FORBIDDEN_JP(
        10,
        NationType.JP,
        "다림질 불가", "/img/ironing/jap_no.png"
    ),
    IRONING_80_120_JP(11, NationType.JP, "80~120도", "/img/ironing/jap_to80120.png"), IRONING_140_160_JP(
        12,
        NationType.JP,
        "140~160도", "/img/ironing/jap_to140160.png"
    ),
    IRONING_180_210_JP(13, NationType.JP, "180~210도", "/img/ironing/jap_to180210.png");
}
