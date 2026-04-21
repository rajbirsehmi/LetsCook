package com.creative.letscook.data.local

import androidx.room.PrimaryKey

data class RecentSearch(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,

)
