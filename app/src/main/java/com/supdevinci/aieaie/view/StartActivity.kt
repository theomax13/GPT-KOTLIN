package com.supdevinci.aieaie.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supdevinci.aieaie.R
import com.supdevinci.aieaie.adapters.ConversationsAdapter
import com.supdevinci.aieaie.model.Conversation

class StartActivity : AppCompatActivity() {

    private lateinit var conversationsAdapter: ConversationsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        recyclerView = findViewById(R.id.conversationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        conversationsAdapter = ConversationsAdapter { conversation ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("conversation_id", conversation.id)
            }
            startActivity(intent)
        }

        recyclerView.adapter = conversationsAdapter

        loadConversations()
    }

    private fun loadConversations() {
        val fakeConversations = listOf(
            Conversation("1", "Conversation 1"),
            Conversation("2", "Conversation 2"),
        )
        conversationsAdapter.submitList(fakeConversations)
    }
}
