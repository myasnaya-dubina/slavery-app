package ru.tzkt.slavery.domain

import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("salaryViewModel")
    val salaryViewModel: SalaryViewModel,
    @SerializedName("mode")
    var mode: TaxCalculator.Mode
) {

    companion object {

        fun defaultConfig(): Config {
            return Config(
                SalaryViewModel(
                    100000,
                    0,
                    900
                ),
                TaxCalculator.Mode.MONTHLY
            )
        }
    }
}