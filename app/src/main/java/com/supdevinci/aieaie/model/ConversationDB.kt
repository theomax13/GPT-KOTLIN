package com.supdevinci.aieaie.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConversationDB(
    val title: String,
    val content: String,
    val dateAdded: Long = System.currentTimeMillis(),
    val updatedAt: Long?,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
