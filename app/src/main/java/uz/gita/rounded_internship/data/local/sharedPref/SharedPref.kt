package uz.gita.rounded_internship.data.local.sharedPref

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.rounded_internship.utils.DIALOG_STATE
import uz.gita.rounded_internship.utils.IS_FIRST
import uz.gita.rounded_internship.utils.SHARED_PREF_NAME
import javax.inject.Singleton

class SharedPref(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setDialogState(state: Boolean) {
        editor.putBoolean(DIALOG_STATE, state).apply()
    }

    fun getDialogState(): Boolean {
        return sharedPreferences.getBoolean(DIALOG_STATE, false)
    }

    fun setIsFirst(state: Boolean) {
        editor.putBoolean(IS_FIRST, state).apply()
    }

    fun getIsFirst(): Boolean {
        return sharedPreferences.getBoolean(IS_FIRST, true)
    }
}