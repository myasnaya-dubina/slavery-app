package ru.tzkt.slavery.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.total_view.view.totalHeader
import kotlinx.android.synthetic.main.total_view.view.totalNumber
import ru.tzkt.slavery.R
import ru.tzkt.slavery.utils.setRubAmount

class TotalAmountView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val headerText: CharSequence

    init {
        inflate(context, R.layout.total_view, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TotalAmountView)
        headerText = typedArray.getText(R.styleable.TotalAmountView_headerText)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        totalHeader.text = headerText
    }

    fun setAmount(number: Int) {
        totalNumber.setRubAmount(number)
    }
}