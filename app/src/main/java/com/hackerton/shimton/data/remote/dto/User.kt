package com.hackerton.shimton.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val nickName: String,
    val password: String
): Parcelable