package com.example.yesican.data

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created : Long = System.currentTimeMillis()
): Parcelable {
    val createdDateFormat: String
        get() = DateFormat.getDateTimeInstance().format(created)
}