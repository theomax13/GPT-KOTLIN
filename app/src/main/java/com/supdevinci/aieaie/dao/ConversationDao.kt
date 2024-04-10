package com.supdevinci.aieaie.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.supdevinci.aieaie.model.ConversationDB
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Upsert
    suspend fun upsert(conversation: ConversationDB)

    @Delete
    suspend fun delete(conversation: ConversationDB)

    @Query("SELECT * FROM conversationDB")
    fun getAll(): Flow<List<ConversationDB>>

    @Query("SELECT * FROM conversationDB WHERE id = :id")
    fun getById(id: Int): ConversationDB
}