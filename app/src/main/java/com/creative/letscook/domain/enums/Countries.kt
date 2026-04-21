package com.creative.letscook.domain.enums

enum class Countries(val displayName: String) {
    MEDITERRANEAN("Mediterranean"),
    CONTINENTAL("Continental"),
    INDIAN("Indian"),
    ITALIAN("Italian"),
    FRENCH("French"),
    CHINESE("Chinese"),
    JAPANESE("Japanese"),
    MEXICAN("Mexican"),
    THAI("Thai"),
    MIDDLE_EASTERN("Middle Eastern"),
    AMERICAN("American"),
    SPANISH("Spanish"),
    GREEK("Greek"),
    KOREAN("Korean"),
    VIETNAMESE("Vietnamese");

    fun toPromptString(): String = displayName
}
