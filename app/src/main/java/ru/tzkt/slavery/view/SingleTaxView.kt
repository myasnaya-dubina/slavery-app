package ru.tzkt.slavery.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.single_tax_view.view.descriptionTaxNameTextView
import kotlinx.android.synthetic.main.single_tax_view.view.taxNameTextView
import kotlinx.android.synthetic.main.single_tax_view.view.taxSumTextView
import ru.tzkt.slavery.R
import ru.tzkt.slavery.utils.setRubAmount

class SingleTaxView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private val taxName: CharSequence
    private val taxDescription: CharSequence

    init {
        inflate(context, R.layout.single_tax_view, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SingleTaxView)
        taxName = typedArray.getText(R.styleable.SingleTaxView_taxName)
        taxDescription = typedArray.getText(R.styleable.SingleTaxView_taxDescription)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        taxNameTextView.text = taxName
        descriptionTaxNameTextView.text = taxDescription
    }

    fun setAmount(amount: Int) {
        taxSumTextView.setRubAmount(amount)
    }
}