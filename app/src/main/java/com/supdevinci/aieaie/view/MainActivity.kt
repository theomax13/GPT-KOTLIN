@file:OptIn(ExperimentalMaterial3Api::class)

package com.supdevinci.aieaie.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.supdevinci.aieaie.ui.theme.AIEAIETheme
import com.supdevinci.aieaie.viewmodel.ChatMessage
import com.supdevinci.aieaie.viewmodel.OpenAiViewModel

class MainActivity : ComponentActivity() {
    private val openAiViewModel = OpenAiViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIEAIETheme {
                MessageScreen(openAiViewModel)
            }
        }
    }
}

@Composable
fun MessageScreen(viewModel: OpenAiViewModel) {
    val conversationHistory by viewModel.conversationHistory.observeAsState(initial = listOf())
    var inputText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            InputBar(inputText, onInputChange = { inputText = it }) {
                if (inputText.isNotBlank()) {
                    viewModel.sendMessage(inputText.trim())
                    inputText = ""
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(conversationHistory) { message ->
                    MessageCard(message)
                }
            }
        }

        LaunchedEffect(conversationHistory.size) {
            if (conversationHistory.isNotEmpty()) {
                scrollState.animateScrollToItem(index = conversationHistory.size - 1)
            }
        }
    }
}

@Composable
fun InputBar(text: String, onInputChange: (String) -> Unit, onSend: () -> Unit) {
    TextField(
        value = text,
        onValueChange = onInputChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Type a message") },
        trailingIcon = {
            IconButton(onClick = onSend) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        )
    )
}

@Composable
fun MessageCard(message: ChatMessage) {
    // Decide the alignment based on the sender
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

    // Set a maximum width that the card can take, e.g., 85% of the parent's width
    val maxWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp * 0.85f }

    // Use the Box composable for alignment
    Box(
        contentAlignment = alignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(max = maxWidth),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

