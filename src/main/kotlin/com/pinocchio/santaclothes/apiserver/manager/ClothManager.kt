package com.pinocchio.santaclothes.apiserver.manager

import com.pinocchio.santaclothes.apiserver.entity.Cloth

class ClothManager {
    fun howTo(cloth: Cloth): ManageTip {
        // val careLabel = cloth.careLabel ?: throw IllegalArgumentException("care label should be existed. $cloth")
        return MANAGE_TIPS.random()
    }

    companion object {
        private val MANAGE_TIPS = listOf(
            ManageTip(
                "폴리에스테르 세탁 방법",
                """
                    폴리에스테르는 단독으로 세탁하는 것이 가장 좋으며 30도 이하의 미지근한 물에 중성세제를 사용하여 세탁하는 게 좋습니다.
                    단시간의 세탁을 권장합니다
                """.trimIndent()
            ),
            ManageTip(
                "나일론 세탁 방법",
                """
                    중성세제를 사용하여 20도의 물에 세탁하는 것이 좋습니다. 표백제는 사용하지 않는 것이 좋습니다. 단시간의 단독 세탁이 가장 좋습니다.
                """.trimIndent()
            ),
            ManageTip(
                "물이 빠지는 옷 세탁 방법",
                """"세탁하기 전 소금물에 30분 정도 담가두세요."""
            ),
            ManageTip(
                "와이셔츠 목의 찌든 때 제거 방법",
                """
                    샴푸를 발라 세탁하면 때가 잘 빠집니다. 셔츠가 다 마른뒤 분말형 땀띠약을 뿌려놓으면 다음 번 세탁시 수월합니다.
                """.trimIndent()
            ),
            ManageTip(
                "니트 손 세탁 방법",
                """
                    첫 세탁 시에 드라이 클리닝을 권장합니다.
                    다음 세탁부터는 미지근한 물에 울 전용 샴푸를 풀어 30분 담가둔 후 부드럽게 눌러서 세탁하면 됩니다.
                    손으로 비비거나 짜서는 안되며 세탁후 타놀에 말아 물기를 제거합니다.
                """.trimIndent()
            )
        )
    }
}

data class ManageTip(
    val title: String,
    val content: String
)
