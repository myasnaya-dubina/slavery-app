package ru.tzkt.slavery.domain

import com.google.gson.annotations.SerializedName

private const val NDFL_PERCENT = 0.13
private const val PENSION_PERCENT = 0.22
private const val MEDICINE_PERSENT = 0.051
private const val FSS_PERCENT = 0.029

class TaxCalculator {

    private val taxes by lazy { Taxes() }
    lateinit var calculatedAction: (Taxes) -> Unit

    var monthlyIncome = 0
        set(value) {
            field = value
            calculate()
        }
    var mode = Mode.MONTHLY
        set(value) {
            field = value
            calculate()
        }

    private fun calculate() {
        val ndfl = (monthlyIncome * NDFL_PERCENT).toInt()
        val pension = (monthlyIncome * PENSION_PERCENT).toInt()
        val medicine = (monthlyIncome * MEDICINE_PERSENT).toInt()
        val fss = (monthlyIncome * FSS_PERCENT).toInt()
        val totalTaxMonthly = ndfl + pension + medicine + fss
        val totalSalaryMonthly = monthlyIncome + pension + medicine + fss
        taxes.also {
            it.ndfl = ndfl
            it.pension = pension
            it.medicine = medicine
            it.fss = fss
            it.totalTax = totalTaxMonthly
            it.totalSalary = totalSalaryMonthly
        }
        if (mode == Mode.YEARLY) {
            taxes.increaseYearly()
        }
        calculatedAction.invoke(taxes)
    }

    enum class Mode {
        @SerializedName("monthly")
        MONTHLY,

        @SerializedName("yearly")
        YEARLY
    }
}