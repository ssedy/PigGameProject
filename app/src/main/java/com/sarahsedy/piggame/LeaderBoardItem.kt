package com.sarahsedy.piggame

import android.os.Parcel
import android.os.Parcelable


class LeaderBoardItem(var player: String, private var score: String, private var date: String) :
    Parcelable {
    fun toCSV(): String {
        return "$player,$score,$date\n"
    }

    override fun toString(): String {
        return player.padEnd(20) + ":".padEnd(3) + score.padEnd(4) + date
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<LeaderBoardItem> {
            override fun createFromParcel(parcel: Parcel) = LeaderBoardItem(parcel)
            override fun newArray(size: Int) = arrayOfNulls<LeaderBoardItem>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        player = parcel.readString().toString(),
        score = parcel.readString().toString(),
        date = parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(player)
        parcel.writeString(score)
        parcel.writeString(date)
    }

    override fun describeContents() = 0
}