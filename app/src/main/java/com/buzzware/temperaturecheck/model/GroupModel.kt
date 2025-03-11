package com.buzzware.temperaturecheck.model

import com.google.android.gms.common.util.Strings

data class GroupModel(
    var comunity : HashMap<String,String> = hashMapOf(),
    var date : Long = 0,
    var groupCount : Long = 0,
    var id : String = "",
    var lastcheckin : Long = 0,
    var name : String = "",
    var pm_id : HashMap<String,String> = hashMapOf(),
    var userId : String = "",
    var userName : String = "",
    var image : String = "",
    )