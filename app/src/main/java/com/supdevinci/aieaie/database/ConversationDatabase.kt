package com.supdevinci.aieaie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.supdevinci.aieaie.dao.ConversationDao
import com.supdevinci.aieaie.model.ConversationDB

@Database(entities = [ConversationDB::class], version = 1)
abstract class ConversationDatabase: RoomDatabase() {
    abstract val dao: ConversationDao
}