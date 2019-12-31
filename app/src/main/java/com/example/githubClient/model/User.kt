package com.example.githubClient.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class User(id: Int?, nodeId: String?, name: String?, login: String?, avatarUrl: String?, company: String?, location: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    @SerializedName("id")
    var id: Int? = null
    @SerializedName("node_id")
    var nodeId: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("login")
    var login: String? = null
    @SerializedName("avatar_url")
    var avatarUrl: String? = null
    @SerializedName("company")
    var company: String? = null
    @SerializedName("location")
    var location: String? = null

    init {
        this.id = id
        this.nodeId = nodeId
        this.name = name
        this.login = login
        this.avatarUrl = avatarUrl
        this.company = company
        this.location = location
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nodeId)
        parcel.writeString(name)
        parcel.writeString(login)
        parcel.writeString(avatarUrl)
        parcel.writeString(company)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

}