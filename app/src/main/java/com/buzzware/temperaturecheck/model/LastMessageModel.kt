package com.buzzware.temperaturecheck.model

data class LastMessageModel(
    var content : String? = "",
    var fromID : String? = "",
    var isRead : HashMap<String,Boolean>? = hashMapOf(),
    var messageId : String? = "",
    var timestamp : Long? = 0,
    var toID : String? = "",
    var type : String? = "",
)
