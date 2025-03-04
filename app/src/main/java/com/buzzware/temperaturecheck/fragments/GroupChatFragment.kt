package com.buzzware.temperaturecheck.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.activities.ChatScreenActivity
import com.buzzware.temperaturecheck.adapters.GroupChatingAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentGroupChatBinding

class GroupChatFragment : BaseFragment(), GroupChatingAdapter.OnClicked{

    val binding : FragmentGroupChatBinding by lazy {
        FragmentGroupChatBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        setViews()


        return binding.root
    }

    private fun setViews() {

        Constants.userGroupChats.sortByDescending { it.lastMessage.timestamp }

        val groupChatsAdapter = GroupChatingAdapter(context, Constants.userGroupChats , this)
        binding.groupChat.layoutManager = LinearLayoutManager(context)
        binding.groupChat.adapter = groupChatsAdapter

    }

    override fun OnItemClicked(chatId: String?, chatName: String) {

        val intent = Intent(context, ChatScreenActivity::class.java)
        intent.putExtra("ChatID",chatId)
        intent.putExtra("chatName", chatName)
        intent.putExtra("SenderID", "3Tm911DhpSOxsYLBiHeC3WciDs52")
        startActivity(intent)
        requireActivity().overridePendingTransition(fadeIn, fadeOut)

    }


}