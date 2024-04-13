package com.supdevinci.aieaie.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.aieaie.dao.ConversationDao
import com.supdevinci.aieaie.model.ConversationDB
import com.supdevinci.aieaie.presentation.ConversationEvent
import com.supdevinci.aieaie.presentation.ConversationState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConversationViewModel(private val dao: ConversationDao): ViewModel() {
    private val _navigationCommands = MutableSharedFlow<NavigationCommand>()
    val navigationCommands: SharedFlow<NavigationCommand> = _navigationCommands.asSharedFlow()

    private var conversations =
        dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(ConversationState())
    val state =
        combine(
            _state,
            dao.getAll()
        ) { state, conversations ->
            state.copy(conversations = conversations)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ConversationState())

    fun onEvent(event: ConversationEvent) {
        when(event) {
            is ConversationEvent.SelectConversation -> {
                // Emit a navigation command
                viewModelScope.launch {
                    _navigationCommands.emit(NavigationCommand.NavigateTo("chat_screen/${event.conversationDB.id}"))
                }
            }
            is ConversationEvent.DeleteConversation -> {
                viewModelScope.launch {
                    try {
                        dao.delete(event.conversationDB)
                    } catch (e: Exception) {
                        // Handle the exception, maybe update UI to show an error message
                    }
                }
            }
            is ConversationEvent.UpdateConversation -> {
                val conversation = ConversationDB(
                    title = event.conversationDB.title,
                    content = event.conversationDB.content,
                    updatedAt = null,
                )
                viewModelScope.launch {
                    try {
                        dao.upsert(conversation)
                    } catch (e: Exception) {
                        // Handle the exception, maybe update UI to show an error message
                    }
                }
                _state.update { currentState ->
                    currentState.copy(title = mutableStateOf(""), content = mutableStateOf(""))
                }
            }
            is ConversationEvent.AddConversation -> {
                val conversation = ConversationDB(
                    title = event.conversationDB.title,
                    content = event.conversationDB.content,
                    updatedAt = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    try {
                        dao.upsert(conversation)
                    } catch (e: Exception) {
                        // Handle the exception, maybe update UI to show an error message
                    }
                }
                _state.update { currentState ->
                    currentState.copy(title = mutableStateOf(""), content = mutableStateOf(""))
                }
            }
        }
    }
}

sealed class NavigationCommand {
    data class NavigateTo(val route: String) : NavigationCommand()
}