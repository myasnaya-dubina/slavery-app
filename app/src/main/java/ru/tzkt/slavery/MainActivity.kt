package ru.tzkt.slavery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.bottomNavView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openScreen(MainScreen.MORTGAGE)

        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_apt -> openScreen(MainScreen.MORTGAGE)
                R.id.navigation_tax -> openScreen(MainScreen.TAX)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun openScreen(screen: MainScreen) {
        var frag = supportFragmentManager.findFragmentByTag(screen.name)
        if (frag == null) {
            frag = createFragment(screen)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, frag, screen.name)
            .commit()
    }

    private fun createFragment(screen: MainScreen): Fragment {
        return when (screen) {
            MainScreen.MORTGAGE -> MortgageFragment.newInstance()
            MainScreen.TAX -> SalaryFragment.newInstance()
        }
    }

    enum class MainScreen {
        MORTGAGE,
        TAX
    }
}
