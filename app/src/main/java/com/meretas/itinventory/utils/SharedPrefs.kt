package com.meretas.itinventory.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    val PREFS_FILENAME = "prefs"
    val AUTH_TOKEN = "authTokenSave"
    val USER_NAME = "userName"
    val USER_BRANCH = "userBranch"
    val IS_READ_ONLY = "userReadOnly"
    val IS_COMPLETE_LOGIN = "userCompleteLogin"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var authTokenSave: String
        get() = prefs.getString(USER_NAME, "") ?: ""
        set(value) = prefs.edit().putString(USER_NAME, value).apply()

    var userNameSave: String
        get() = prefs.getString(USER_BRANCH, "") ?: ""
        set(value) = prefs.edit().putString(USER_BRANCH, value).apply()

    var userBranchSave: String
        get() = prefs.getString(AUTH_TOKEN, "") ?: ""
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var isReadOnly: Boolean
        get() = prefs.getBoolean(IS_READ_ONLY,false)
        set(value) = prefs.edit().putBoolean(IS_READ_ONLY, value).apply()

    var isCompleteLogin: Boolean
        get() = prefs.getBoolean(IS_COMPLETE_LOGIN,false)
        set(value) = prefs.edit().putBoolean(IS_COMPLETE_LOGIN, value).apply()

    //    val IS_LOGGED_IN = "isLoggedIn"
    //    var isLoggedIn: Boolean
    //        get() = prefs.getBoolean(IS_LOGGED_IN, false)
    //        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
}