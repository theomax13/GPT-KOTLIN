package com.supdevinci.aieaie.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.aieaie.model.OpenAiMessageBody
import com.supdevinci.aieaie.model.request.BodyToSend
import com.supdevinci.aieaie.model.response.GeneratedAnswer
import com.supdevinci.aieaie.repository.OpenAiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val content: String,
    val isUser: Boolean  // true if the message is from the user, false if from the AI
)
class OpenAiViewModel : ViewModel() {
    private val repository = OpenAiRepository()
    private val _openAiResponse = MutableStateFlow<GeneratedAnswer?>(null)
    private val _conversationHistory = MutableLiveData<List<ChatMessage>>()
    val conversationHistory: LiveData<List<ChatMessage>> = _conversationHistory

    val openAiResponse: StateFlow<GeneratedAnswer?> = _openAiResponse

    init {
        _conversationHistory.value = listOf()
    }

    fun fetchMessages() {
        viewModelScope.launch {
            try {
                val bodyToSend = BodyToSend(messages = listOf(OpenAiMessageBody(role= "assistant", content= "test test")))
                _openAiResponse.value = repository.getChatFromOpenAi(bodyToSend)
                Log.e("Fetch Messages List : ", _openAiResponse.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("Fetch Contact List : ", e.message.toString())
            }
        }
    }

    fun sendMessage(userMessage: String) {
        val userMessageToAdd = ChatMessage(content = userMessage, isUser = true)
        addMessageToHistory(userMessageToAdd)

        viewModelScope.launch {
            try {
                val promptWithHistory = buildPromptWithHistory(userMessage)

                val bodyToSend = BodyToSend(messages = listOf(OpenAiMessageBody(role= "user", content= promptWithHistory)))

                val response = repository.getChatFromOpenAi(bodyToSend)

                response.choices.forEach { choice ->
                    val aiMessageToAdd = ChatMessage(content = choice.message.content, isUser = false)
                    addMessageToHistory(aiMessageToAdd)
                }
            } catch (e: Exception) {
                Log.e("Send Message : ", e.message.toString())
            }
        }
    }

    private fun buildPromptWithHistory(userMessage: String): String {
        val history = conversationHistory.value.orEmpty()
        val sb = StringBuilder()

        for (message in history) {
            sb.append(if (message.isUser) "User: " else "AI: ")
            sb.append(message.content)
            sb.append("\n")
        }

        sb.append("User: $userMessage\n")

        return sb.toString()
    }

    private fun addMessageToHistory(message: ChatMessage) {
        val updatedHistory = _conversationHistory.value.orEmpty() + message
        _conversationHistory.postValue(updatedHistory)
    }
}