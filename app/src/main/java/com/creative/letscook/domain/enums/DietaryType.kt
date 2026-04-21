package com.creative.letscook.domain.enums

enum class DietaryType(val displayName: String, val description: String) {
    NONE("None", "No dietary restrictions"),
    VEGAN("Vegan", "No animal products (meat, dairy, eggs, or honey)"),
    VEGETARIAN("Vegetarian", "No meat or fish, but includes dairy and eggs"),
    NON_VEG("Non-Vegetarian", "Includes meat, poultry, and seafood"),
    PESCATARIAN("Pescatarian", "Vegetarian diet that includes fish and seafood"),
    KETO("Keto", "High-fat, low-carbohydrate focus"),
    PALEO("Paleo", "Focus on lean meats, fish, fruits, and vegetables (no grains/dairy)"),
    GLUTEN_FREE("Gluten-Free", "No wheat, barley, or rye"),
    DAIRY_FREE("Dairy-Free", "No milk-based products");

    // Useful for UI labels or Groq prompts
    fun toPrompt(): String = displayName
}