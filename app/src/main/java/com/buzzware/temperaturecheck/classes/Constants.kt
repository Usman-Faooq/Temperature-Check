package com.buzzware.temperaturecheck.classes

import com.buzzware.temperaturecheck.model.ChatModel
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel


object Constants {

    var currentUser : UserModel = UserModel()
    var currentUserQuestion : ArrayList<UserQuestionModel> = ArrayList()
    var userPersonalChats : ArrayList<ChatModel> = ArrayList()
    var userGroupChats : ArrayList<ChatModel> = ArrayList()
    var selectedType : String = ""
    var answerList: HashMap<Int,String> = hashMapOf()

}