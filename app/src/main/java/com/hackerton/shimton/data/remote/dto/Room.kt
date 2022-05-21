package com.hackerton.shimton.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("password") val password: String
) {
    @SerializedName("title") var name: String =""
    @SerializedName("boardIdx") var id: Long = -1

    constructor(id: Long, name: String, password: String) : this(
        password
    ) {
        this.name = name
        this.id = id
    }

    constructor(name: String, password: String) : this(
        password
    ) {
        this.name = name
        this.id = id
    }

    constructor(id: Long, password: String) : this(
        password
    ) {
        this.id = id
        this.name = name
    }
}