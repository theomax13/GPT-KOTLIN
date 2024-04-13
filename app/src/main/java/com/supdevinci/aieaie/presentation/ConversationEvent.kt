package com.supdevinci.aieaie.presentation

import com.supdevinci.aieaie.model.ConversationDB

sealed interface ConversationEvent {
    data class DeleteConversation(val conversationDB: ConversationDB): ConversationEvent

    data class UpdateConversation(val conversationDB: ConversationDB): ConversationEvent

    data class AddConversation(val conversationDB: ConversationDB): ConversationEvent

    data class SelectConversation(val conversationDB: ConversationDB): ConversationEvent

}