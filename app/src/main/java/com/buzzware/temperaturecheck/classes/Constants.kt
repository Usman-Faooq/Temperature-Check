package com.buzzware.temperaturecheck.classes

import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel


object Constants {

    var currentUser : UserModel = UserModel()
    var currentUserQuestion : ArrayList<UserQuestionModel> = ArrayList()
    var selectedType : String = ""

}