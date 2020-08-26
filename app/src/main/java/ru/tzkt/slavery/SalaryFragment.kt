package ru.tzkt.slavery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_salary.salaryView
import kotlinx.android.synthetic.main.fragment_salary.taxesView
import ru.tzkt.slavery.data.Preferences
import ru.tzkt.slavery.domain.Config
import ru.tzkt.slavery.domain.TaxCalculator

class SalaryFragment : Fragment() {

    companion object {

        fun newInstance() = SalaryFragment()
    }

    private val taxCalculator by lazy { TaxCalculator() }
    private val preferences by lazy { Preferences(requireContext(), Gson()) }
    private lateinit var config: Config

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_salary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taxCalculator.calculatedAction = { taxes -> taxesView.populate(taxes) }
        salaryView.salaryChangesAction = { progress -> taxCalculator.monthlyIncome = progress }
        taxesView.modeChangesAction = { mode ->
            taxCalculator.mode = mode
            config.mode = mode
        }
    }

    override fun onStart() {
        super.onStart()

        config = preferences.loadConfig()
        salaryView.salaryViewModel = config.salaryViewModel
        salaryView.populate()
        taxesView.setMode(config.mode)
    }

    override fun onStop() {
        super.onStop()

//        config.salaryViewModel.topBorder = salaryView.salaryViewModel.topBorder
//        config.salaryViewModel.bottomBorder = salaryView.salaryViewModel.bottomBorder
//        config.salaryViewModel.currentSalary = salaryView.salaryViewModel.currentSalary

        preferences.saveConfig(config)
    }
}