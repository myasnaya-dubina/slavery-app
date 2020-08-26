package ru.tzkt.slavery.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.invalid_salary_borders_dialog.view.okButton
import kotlinx.android.synthetic.main.number_input_dialog.view.cancelButton
import kotlinx.android.synthetic.main.number_input_dialog.view.confirmButton
import kotlinx.android.synthetic.main.number_input_dialog.view.inputNumberEditText
import kotlinx.android.synthetic.main.number_input_dialog.view.numberInputDialogHeader
import ru.tzkt.slavery.R
import java.text.DecimalFormat
import kotlin.reflect.KClass

val formatter by lazy { DecimalFormat("#,###") }

fun TextView.setRubAmount(number: Int) {
    val result = "${formatter.format(number)} р.".replace(",", " ")
    text = result
}

fun TextView.setPercentAmount(number: Float) {
    val formatted = String.format("%.2f", number); // 4.00
    text = "$formatted %"
}

fun Context.showInputNumberDialog(header: String, initialValue: Int, confirmAction: (Int) -> Unit) {
    val view = LayoutInflater.from(this).inflate(R.layout.number_input_dialog, null)
    val dialog = AlertDialog.Builder(this, R.style.DialogStyle)
        .setView(view)
        .create()
    val inputEditText = view.inputNumberEditText
    inputEditText.requestFocus()
    inputEditText.setText(initialValue.toString())

    view.confirmButton.setOnClickListener {
        val input = inputEditText.text.toString().toInt()
        dialog.dismiss()
        confirmAction.invoke(input)
    }
    view.cancelButton.setOnClickListener { dialog.dismiss() }
    dialog.setOnDismissListener {

    }
    view.numberInputDialogHeader.text = header
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    dialog.show()
}

fun <T : Fragment> KClass<T>.getSimpleTag(): String = java.name

fun SeekBar.setDefaultStyle() {
    progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
    thumb.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
}

fun Context.showInvalidBordersDialog() {
    val view = LayoutInflater.from(this).inflate(R.layout.invalid_salary_borders_dialog, null)
    val dialog = AlertDialog.Builder(this, R.style.DialogStyle)
        .setView(view)
        .create()
    view.okButton.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}