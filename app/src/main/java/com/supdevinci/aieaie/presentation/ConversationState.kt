package com.supdevinci.aieaie.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.supdevinci.aieaie.model.ConversationDB

data class ConversationState (
    val conversations: List<ConversationDB> = emptyList(),
    var title: MutableState<String> = mutableStateOf(""),
    val content: MutableState<String> = mutableStateOf(""),
)