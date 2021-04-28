package com.pinocchio.santaclothes.apiserver.entity.type

enum class DryCleaning(var code: Int, var nationType: NationType, var description: String) {
    DRY_CLEANING_KR(0, NationType.KR, "드라이 클리닝 가능"), DRY_CLEANING_FORBIDDEN_KR(
        1,
        NationType.KR,
        "드라이 클리닝 불가"
    ),
    DRY_CLEANING_OIL_KR(2, NationType.KR, "석유계 드라이 클리닝 가능"), DRY_CLEANING_SELF_FORBIDDEN_KR(
        3,
        NationType.KR,
        "셀프 드라이 클리닝 불가"
    ),
    DRY_CLEANING_USA(4, NationType.USA, "드라이 클리닝 가능"), DRY_CLEANING_FORBIDDEN_USA(
        5,
        NationType.USA,
        "드라이 클리닝 불가"
    ),
    DRY_CLEANING_SHORT_USA(6, NationType.USA, "짧은 드라이 클리닝"), DRY_CLEANING_LOW_HUMIDITY_USA(
        7,
        NationType.USA,
        "낮은 습도에서 드라이 클리닝"
    ),
    DRY_CLEANING_NOT_HOT_USA(8, NationType.USA, "열을 줄여서 드라이 클리닝"), DRY_CLEANING_JP(
        9,
        NationType.JP,
        "드라이 클리닝 가능"
    ),
    DRY_CLEANING_FORBIDDEN_JP(10, NationType.JP, "드라이 클리닝 불가");
}
