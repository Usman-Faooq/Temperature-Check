package com.buzzware.temperaturecheck.model


data class ChatModel(
    var chatId : String? = "",
    var chatType : String? = "",
    var groupName : String? = "",
    var lastMessage : LastMessageModel = LastMessageModel(),
    var participants : HashMap<String,Boolean> = hashMapOf(),
    var senderID : String? = "",
    )