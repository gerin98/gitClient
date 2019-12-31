package com.example.githubClient.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Repo(id: Int?, nodeId: String?, name: String?, fullName: String?, htmlUrl: String?) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    companion object CREATOR : Parcelable.Creator<Repo> {
        override fun createFromParcel(parcel: Parcel): Repo {
            return Repo(parcel)
        }

        override fun newArray(size: Int): Array<Repo?> {
            return arrayOfNulls(size)
        }
    }

    @SerializedName("id")
    var id: Int? = null
    @SerializedName("node_id")
    var nodeId: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("full_name")
    var fullName: String? = null
    @SerializedName("html_url")
    var htmlUrl: String? = null

    init {
        this.id = id
        this.nodeId = nodeId
        this.name = name
        this.fullName = fullName
        this.htmlUrl = htmlUrl
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nodeId)
        parcel.writeString(name)
        parcel.writeString(fullName)
        parcel.writeString(htmlUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

}