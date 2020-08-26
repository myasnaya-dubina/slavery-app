package ru.tzkt.slavery.data

import android.content.Context
import com.google.gson.Gson
import ru.tzkt.slavery.domain.Config

private const val CONFIG_PREF_KEY = "app_configuration"

class Preferences(
    private val context: Context,
    private val gson: Gson
) {

    fun saveConfig(config: Config) {
        val configStr = gson.toJson(config)
        getPrefs()
            .edit()
            .putString(CONFIG_PREF_KEY, configStr)
            .apply()
    }

    fun loadConfig(): Config {
        val configStr = getPrefs().getString(CONFIG_PREF_KEY, null)
        return if (configStr == null) {
            Config.defaultConfig()
        } else {
            gson.fromJson(configStr, Config::class.java)
        }
    }

    private fun getPrefs() = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

}