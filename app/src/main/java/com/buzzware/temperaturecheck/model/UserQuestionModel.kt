package com.buzzware.temperaturecheck.model

import android.os.Parcel
import android.os.Parcelable

data class UserQuestionModel(
    var answer: String = "",
    var date: Long = 0,
    var date1: String = "",
    var id: String = "",
    var question: String = "",
    var seq: Long = 0,
    var today: String = "",
    var userId: String = "",
    var avg: Double = 0.0,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(answer)
        parcel.writeLong(date)
        parcel.writeString(date1)
        parcel.writeString(id)
        parcel.writeString(question)
        parcel.writeLong(seq)
        parcel.writeString(today)
        parcel.writeString(userId)
        parcel.writeDouble(avg)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UserQuestionModel> {
        override fun createFromParcel(parcel: Parcel): UserQuestionModel {
            return UserQuestionModel(parcel)
        }

        override fun newArray(size: Int): Array<UserQuestionModel?> {
            return arrayOfNulls(size)
        }
    }
}
