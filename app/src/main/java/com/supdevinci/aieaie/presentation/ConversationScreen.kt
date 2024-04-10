package com.supdevinci.aieaie.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ConversationScreen(state: ConversationState, navController: NavController, onEvent: (ConversationEvent) -> Unit) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.title.value = ""
                state.content.value = ""
                navController.navigate("add_conversation")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add conversation")
            }

        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            items(state.conversations.size) { index ->
                ConversationItem(
                    state = state,
                    index = index,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun ConversationItem(state: ConversationState, index: Int, onEvent: (ConversationEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = state.conversations[index].title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.weight(1f))
        
        IconButton(onClick = { onEvent(ConversationEvent.DeleteConversation(state.conversations[index])) }) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete conversation", modifier = Modifier.size(35.dp))
        }
    }
}
