package ru.tzkt.slavery.domain

import com.google.gson.annotations.SerializedName

data class SalaryViewModel(
    @SerializedName("currentSalary")
    var currentSalary: Int = 0,
    @SerializedName("bottomBorder")
    var bottomBorder: Int = 0,
    @SerializedName("topBorder")
    var topBorder: Int = 0
)