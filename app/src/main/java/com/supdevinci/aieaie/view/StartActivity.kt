package com.supdevinci.aieaie.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.supdevinci.aieaie.database.ConversationDatabase
import com.supdevinci.aieaie.presentation.AddConversationScreen
import com.supdevinci.aieaie.presentation.ConversationScreen
import com.supdevinci.aieaie.ui.theme.AIEAIETheme
import com.supdevinci.aieaie.viewmodel.ConversationViewModel

class StartActivity : AppCompatActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            ConversationDatabase::class.java,
            "conversation.db"
        ).build()
    }

    private val viewModel by viewModels<ConversationViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ConversationViewModel(database.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedinstanceState: Bundle?) {
        super.onCreate(savedinstanceState)
        setContent {
            AIEAIETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val state by viewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "conversations") {
                        composable("conversations") {
                            ConversationScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable("add_conversation") {
                            AddConversationScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}
