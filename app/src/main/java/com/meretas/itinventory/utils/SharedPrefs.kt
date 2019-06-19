package com.meretas.itinventory.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    val AUTH_TOKEN = "authToken"
    val USER_NAME = "userName"
    val USER_BRANCH = "userBranch"

    var authToken: String
        get() = prefs.getString(USER_NAME, "") ?: ""
        set(value) = prefs.edit().putString(USER_NAME, value).apply()

    var userNameSave: String
        get() = prefs.getString(USER_BRANCH, "") ?: ""
        set(value) = prefs.edit().putString(USER_BRANCH, value).apply()

    var userBranchSave: String
        get() = prefs.getString(AUTH_TOKEN, "") ?: ""
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    //    val IS_LOGGED_IN = "isLoggedIn"
    //    var isLoggedIn: Boolean
    //        get() = prefs.getBoolean(IS_LOGGED_IN, false)
    //        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
}