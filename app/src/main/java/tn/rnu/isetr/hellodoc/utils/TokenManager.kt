
package tn.rnu.isetr.hellodoc.utils

import android.content.Context

object TokenManager {

    private const val PREF_NAME = "HelloDocPrefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_ROLE = "role"

    fun saveAuthData(context: Context, token: String, role: String) {
        val sharedPreferences = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).getString(KEY_TOKEN, null)
    }

    fun getRole(context: Context): String? {
        return context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).getString(KEY_ROLE, null)
    }

    fun clearAuthData(context: Context) {
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit().clear().apply()
    }
}
