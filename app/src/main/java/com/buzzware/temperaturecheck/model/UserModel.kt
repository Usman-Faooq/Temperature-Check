package com.buzzware.temperaturecheck.model

import android.os.Parcel
import android.os.Parcelable

data class UserModel(
    var Afternoon: Long = 0,
    var AfternoonCheckin: Boolean = false,
    var Evening: Long = 0,
    var EveningCheckin: Boolean = false,
    var Morning: Long = 0,
    var MorningCheckin: Boolean = false,
    var authToken: String = "",
    var deviceType: String = "",
    var dob: Long = 0,
    var email: String = "",
    var firstName: String = "",
    var groupCount: Long = 0,
    var id: String = "",
    var image: String = "",
    var isApproved: Boolean = false,
    var isOnline: Boolean = false,
    var isSubsCribed: Boolean = false,
    var lastName: String = "",
    var lastcheckin: Long = 0,
    var password: String = "",
    var phoneNumber: String = "",
    var pm_id: String = "",
    var stripeCustid: String = "",
    var stripeStatus: String = "",
    var stripeaccount_id: String = "",
    var token: String = "",
    var type: String = "",
    var userDate: Long = 0,
    var userName: String = "",
    var userRole: String = "",
    var userprogress: Long = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong()?:0,
        parcel.readByte() == 1.toByte(),
        parcel.readLong()?:0,
        parcel.readByte() == 1.toByte(),
        parcel.readLong()?:0,
        parcel.readByte() == 1.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()?:0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()?:0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() == 1.toByte(),
        parcel.readByte() == 1.toByte(),
        parcel.readByte() == 1.toByte(),
        parcel.readString() ?: "",
        parcel.readLong()?:0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()?:0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(Afternoon)
        parcel.writeByte(if (AfternoonCheckin) 1 else 0)
        parcel.writeLong(Evening)
        parcel.writeByte(if (EveningCheckin) 1 else 0)
        parcel.writeLong(Morning)
        parcel.writeByte(if (MorningCheckin) 1 else 0)
        parcel.writeString(authToken)
        parcel.writeString(deviceType)
        parcel.writeLong(dob)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeLong(groupCount)
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeByte(if (isApproved) 1 else 0)
        parcel.writeByte(if (isOnline) 1 else 0)
        parcel.writeByte(if (isSubsCribed) 1 else 0)
        parcel.writeString(lastName)
        parcel.writeLong(lastcheckin)
        parcel.writeString(password)
        parcel.writeString(phoneNumber)
        parcel.writeString(pm_id)
        parcel.writeString(stripeCustid)
        parcel.writeString(stripeStatus)
        parcel.writeString(stripeaccount_id)
        parcel.writeString(token)
        parcel.writeString(type)
        parcel.writeLong(userDate)
        parcel.writeString(userName)
        parcel.writeString(userRole)
        parcel.writeLong(userprogress)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}
