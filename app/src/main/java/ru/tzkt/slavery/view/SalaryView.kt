package ru.tzkt.slavery.view

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.salary_view.view.bottomSalaryBorder
import kotlinx.android.synthetic.main.salary_view.view.salarySeekBar
import kotlinx.android.synthetic.main.salary_view.view.salaryTextView
import kotlinx.android.synthetic.main.salary_view.view.topSalaryBorder
import ru.tzkt.slavery.R
import ru.tzkt.slavery.domain.SalaryViewModel
import ru.tzkt.slavery.utils.setDefaultStyle
import ru.tzkt.slavery.utils.setRubAmount
import ru.tzkt.slavery.utils.showInputNumberDialog
import ru.tzkt.slavery.utils.showInvalidBordersDialog

class SalaryView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0)
    : ConstraintLayout(context, attrs, defStyleRes) {

    lateinit var salaryChangesAction: (Int) -> Unit

    lateinit var salaryViewModel: SalaryViewModel

    init {
        inflate(context, R.layout.salary_view, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setupSeekBar()
        background = ContextCompat.getDrawable(context, R.drawable.blue_rounded_rectangle)
        salaryTextView.setOnClickListener {
            val header = context.getString(R.string.number_input_dialog_in_rubles)
            context.showInputNumberDialog(header, salaryViewModel.currentSalary) { input ->
                salaryTextView.setRubAmount(input)
                salarySeekBar.progress = calculateProgress(input)
                salaryChangesAction.invoke(input)
            }
        }
        topSalaryBorder.setOnClickListener {
            val header = context.getString(R.string.number_input_dialog_in_thousands_of_rubles)
            context.showInputNumberDialog(header, salaryViewModel.topBorder) {

                if (isSalaryBordersInvalid(salaryViewModel.bottomBorder, it)) {
                    context.showInvalidBordersDialog()
                    return@showInputNumberDialog
                }

                topSalaryBorder.text = formatSalaryBorder(it)
                salaryViewModel.topBorder = it
            }
        }
        bottomSalaryBorder.setOnClickListener {
            val header = context.getString(R.string.number_input_dialog_in_thousands_of_rubles)
            context.showInputNumberDialog(header, salaryViewModel.bottomBorder) {

                if (isSalaryBordersInvalid(it, salaryViewModel.topBorder)) {
                    context.showInvalidBordersDialog()
                    return@showInputNumberDialog
                }

                bottomSalaryBorder.text = formatSalaryBorder(it)
                salaryViewModel.bottomBorder = it
            }
        }
    }

    private fun calculateProgress(salary: Int): Int {
        val point = (salaryViewModel.topBorder - salaryViewModel.bottomBorder) * 1000 / 100F
        return (salary / point).toInt()
    }

    private fun isSalaryBordersInvalid(bottomBorder: Int, topBorder: Int) = bottomBorder >= topBorder

    private fun setupSeekBar() {
        salarySeekBar.setDefaultStyle()
        salarySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                salaryViewModel.currentSalary = calculateCurrentAmount(progress)
                salaryTextView.setRubAmount(salaryViewModel.currentSalary)
                salaryChangesAction.invoke(salaryViewModel.currentSalary)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun calculateCurrentAmount(progress: Int): Int {
        return ((salaryViewModel.topBorder - salaryViewModel.bottomBorder) * 1000 / 100F * progress).toInt() + salaryViewModel.bottomBorder * 1000
    }

    fun populate() {
        salaryTextView.setRubAmount(salaryViewModel.currentSalary)
        bottomSalaryBorder.text = formatSalaryBorder(salaryViewModel.bottomBorder)
        topSalaryBorder.text = formatSalaryBorder(salaryViewModel.topBorder)
        salarySeekBar.progress = calculateProgress(salaryViewModel.currentSalary)
        salaryChangesAction.invoke(salaryViewModel.currentSalary)
    }

    private fun formatSalaryBorder(amount: Int) = "${amount}k"
}