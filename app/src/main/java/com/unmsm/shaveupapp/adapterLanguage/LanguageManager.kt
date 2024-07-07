package com.unmsm.shaveupapp.adapterLanguage
import android.content.Context
import androidx.preference.PreferenceManager
import java.util.Locale

object LanguageManager {

    private const val KEY_SELECTED_LANGUAGE = "selected_language"

    fun getSelectedLanguage(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(KEY_SELECTED_LANGUAGE, "es") ?: "es"
    }

    fun setSelectedLanguage(context: Context, languageCode: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putString(KEY_SELECTED_LANGUAGE, languageCode).apply()
        updateLocale(context, languageCode)
    }

    fun updateLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}