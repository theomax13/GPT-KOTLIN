package com.supdevinci.aieaie.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.supdevinci.aieaie.R
import com.supdevinci.aieaie.model.Conversation

class ConversationsAdapter(private val onConversationClick: (Conversation) -> Unit) :
    RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder>() {

    private var conversations: List<Conversation> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.bind(conversation, onConversationClick)
    }

    override fun getItemCount() = conversations.size

    fun submitList(newConversations: List<Conversation>) {
        conversations = newConversations
        notifyDataSetChanged()
    }

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(conversation: Conversation, onClick: (Conversation) -> Unit) {
            textView.text = conversation.title
            itemView.setOnClickListener { onClick(conversation) }
        }
    }
}
