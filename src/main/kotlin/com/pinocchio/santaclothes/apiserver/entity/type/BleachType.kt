package com.pinocchio.santaclothes.apiserver.entity.type

enum class BleachType(var code: Int, var nationType: NationType, var description: String, var imageUrl:String) {
    FORBIDDEN_KR(0, NationType.KR, "산소, 염소계 표백제로 표백이 불가합니다", "https://cdn.santaclothes.net/bleach/allno.png"), CL_KR(1, NationType.KR, "산소계 표백제로 표백이 불가합니다", "https://cdn.santaclothes.net/bleach/spno.png"), O2_KR(
        2,
        NationType.KR,
        "염소계 표백제로 표백이 불가합니다", "https://cdn.santaclothes.net/bleach/ypno.png"
    ),
    ALL_KR(3, NationType.KR, "산소, 염소계 표백제로 표백하세요", "https://cdn.santaclothes.net/bleach/ysp.png"), O2_BLEACH_KR(4, NationType.KR, "산소계 표백제로 표백하세요", "https://cdn.santaclothes.net/bleach/sp.png"), CL_BLEACH_KR(
        5,
        NationType.KR,
        "염소계 표백제로 표백하세요", "https://cdn.santaclothes.net/bleach/yp.png"
    ),
    FORBIDDEN_USA(6, NationType.USA, "산소, 염소계 표백제로 표백이 불가합니다", "https://cdn.santaclothes.net/bleach/ame_no.png"), O2_COLOR_USA(7, NationType.USA, "비염소제와 색상보호 표백제로 표백하세요", "https://cdn.santaclothes.net/bleach/ame_half.png"), ALL_USA(
        8,
        NationType.USA,
        "산소, 염소계 표백제로 표백하세요", "https://cdn.santaclothes.net/bleach/ame_allbleach.png"
    ),
    FORBIDDEN_JP(9, NationType.JP, "산소, 염소계 표백제로 표백이 불가합니다", "https://cdn.santaclothes.net/bleach/jap_no.png"), ALL_JP(10, NationType.JP, "산소, 염소계 표백제로 표백하세요", "https://cdn.santaclothes.net/bleach/jap_can.png");
}
