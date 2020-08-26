package ru.tzkt.slavery.view

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.taxes_view.view.beforeTaxesAmountView
import kotlinx.android.synthetic.main.taxes_view.view.periodSwitch
import kotlinx.android.synthetic.main.taxes_view.view.taxFss
import kotlinx.android.synthetic.main.taxes_view.view.taxMedicine
import kotlinx.android.synthetic.main.taxes_view.view.taxNdfl
import kotlinx.android.synthetic.main.taxes_view.view.taxPension
import kotlinx.android.synthetic.main.taxes_view.view.totalTaxAmountView
import ru.tzkt.slavery.R
import ru.tzkt.slavery.domain.TaxCalculator
import ru.tzkt.slavery.domain.Taxes

class TaxesView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    CardView(context, attrs, defStyleAttr) {

    lateinit var modeChangesAction: (TaxCalculator.Mode) -> Unit

    init {
        inflate(context, R.layout.taxes_view, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        periodSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) {
                TaxCalculator.Mode.YEARLY
            } else {
                TaxCalculator.Mode.MONTHLY
            }
            modeChangesAction.invoke(mode)
        }
    }

    fun setMode(mode: TaxCalculator.Mode) {
        periodSwitch.isChecked = mode == TaxCalculator.Mode.YEARLY
    }

    fun populate(taxes: Taxes) {
        taxNdfl.setAmount(taxes.ndfl)
        taxPension.setAmount(taxes.pension)
        taxMedicine.setAmount(taxes.medicine)
        taxFss.setAmount(taxes.fss)
        totalTaxAmountView.setAmount(taxes.totalTax)
        beforeTaxesAmountView.setAmount(taxes.totalSalary)
    }
}