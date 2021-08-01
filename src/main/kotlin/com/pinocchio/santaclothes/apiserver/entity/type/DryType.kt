package com.pinocchio.santaclothes.apiserver.entity.type

enum class DryType(var code: Int, var nationType: NationType, var description: String, var imageUrl: String) {
    DRY_SUNNY_HANGER_KR(0, NationType.KR, "햇빛에 옷걸이에 걸어서 건조하세요", "https://cdn.santaclothes.net/dry/hangsun.png"),
    DRY_CLOUD_HANGER_KR(
        1,
        NationType.KR,
        "그늘에서 옷걸이에 걸어서 건조하세요", "https://cdn.santaclothes.net/dry/hang.png"
    ),
    DRY_SUNNY_DOWN_KR(2, NationType.KR, "햇빛에 바닥에 뉘어서 건조하세요", "https://cdn.santaclothes.net/dry/laysun.png"),
    DRY_CLOUD_DOWN_KR(
        3,
        NationType.KR,
        "그늘에서 바닥에 뉘어서 건조하세요",
        "https://cdn.santaclothes.net/dry/lay.png"
    ),
    DRY_BLOWER_KR(
        4,
        NationType.KR,
        "비틀기가 가능합니다", "https://cdn.santaclothes.net/dry/squeeze.png"
    ),
    DRY_BLOWER_FORBIDDEN_KR(5, NationType.KR, "비틀기가 불가합니다", "https://cdn.santaclothes.net/dry/nosqueeze.png"), DRY_MACHINE_KR(
        6,
        NationType.KR,
        "기계 건조가 가능합니다", "https://cdn.santaclothes.net/dry/machine.png"
    ),
    DRY_MACHINE_FORBIDDEN_KR(7, NationType.KR, "기계 건조가 불가합니다", "https://cdn.santaclothes.net/dry/nomachine.png"), DRY_MACHINE_USA(
        8,
        NationType.USA,
        "기계 건조가 가능합니다", "https://cdn.santaclothes.net/dry/machine.png"
    ),
    DRY_MACHINE_FORBIDDEN_USA(9, NationType.USA, "기계 건조가 불가합니다", "https://cdn.santaclothes.net/dry/ame_nomachine.png"), DRY_WET_HANGER_USA(
        10,
        NationType.USA,
        "젖은 채로 줄이나 막대기에 널어서 건조하세요", "https://cdn.santaclothes.net/dry/ame_wetroap.png"
    ),
    DRY_HANGER_USA(11, NationType.USA, "줄에 널어서 건조하세요", "https://cdn.santaclothes.net/dry/ame_roap.png"),
    DRY_DOWN_USA(
        12,
        NationType.USA,
        "평평하게 펴서 건조하세요",
        "https://cdn.santaclothes.net/dry/ame_flat.png"
    ),
    DRY_SHADE_USA(
        13,
        NationType.USA,
        "그늘에서 건조하세요", "https://cdn.santaclothes.net/dry/ame_gloomy.png"
    ),
    DRY_HANGER_JP(14, NationType.JP, "옷걸이에 걸어서 건조하세요", "https://cdn.santaclothes.net/dry/jap_hang.png"),
    DRY_CLOUD_HANGER_JP(
        15,
        NationType.JP,
        "그늘에서 옷걸이에 걸어서 건조하세요", "https://cdn.santaclothes.net/dry/jap_hanggloomy.png"
    ),
    DRY_DOWN_JP(16, NationType.JP, "뉘어서 건조하세요", "https://cdn.santaclothes.net/dry/jap_lay.png"),
    DRY_CLOUD_DOWN_JP(
        17,
        NationType.JP,
        "그늘에서 뉘어서 건조하세요",
        "https://cdn.santaclothes.net/dry/jap_laygloomy.png"
    );
}
