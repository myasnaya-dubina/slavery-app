package ru.tzkt.slavery.domain

private const val MONTHS_IN_YEAR = 12

data class Taxes(
    var ndfl: Int = 0,
    var pension: Int = 0,
    var medicine: Int = 0,
    var fss: Int = 0,
    var totalTax: Int = 0,
    var totalSalary: Int = 0
) {
    fun increaseYearly() {
        ndfl *= MONTHS_IN_YEAR
        pension *= MONTHS_IN_YEAR
        medicine *= MONTHS_IN_YEAR
        fss *= MONTHS_IN_YEAR
        totalTax *= MONTHS_IN_YEAR
        totalSalary *= MONTHS_IN_YEAR
    }
}