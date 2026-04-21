package com.creative.letscook.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GroqRequest(
    val model: String = "llama-3.1-8b-instant",
    val messages: List<GroqMessage>,
    val response_format: GroqResponseFormat = GroqResponseFormat("json_object"),
    val temperature: Double = 0.7
)

@Serializable
data class GroqMessage(
    val role: String,
    val content: String
)

@Serializable
data class GroqJsonSchema(
    val name: String,
    val schema: JsonObject,
    val strict: Boolean? = null
)

@Serializable
data class GroqResponseFormat(
    val type: String,
    val json_schema: GroqJsonSchema? = null
)

@Serializable
data class GroqResponse(
    val choices: List<GroqChoice>
)

@Serializable
data class GroqChoice(
    val message: GroqMessage
)

@Serializable
data class RecipeListWrapper(
    val recipes: List<com.creative.letscook.data.local.RecipeEntity>
)