package com.meretas.itinventory.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    private val PREFSFILENAME = "prefs"
    private val AUTHTOKEN = "authTokenSave"
    private val AUTHTOKEN1 = "authTokenSave_one"
    private val AUTHTOKEN2 = "authTokenSave_two"


    private val USERNAME = "userName"
    private val USERBRANCH = "userBranch"
    private val ISREADONLY = "userReadOnly"
    private val ISCOMPLETELOGIN = "userCompleteLogin"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFSFILENAME, 0)

    var authTokenSave: String
        get() = prefs.getString(AUTHTOKEN, "") ?: ""
        set(value) = prefs.edit().putString(AUTHTOKEN, value).apply()

    var authTokenOne: String
        get() = prefs.getString(AUTHTOKEN1, "") ?: ""
        set(value) = prefs.edit().putString(AUTHTOKEN1, value).apply()

    var authTokenTwo: String
        get() = prefs.getString(AUTHTOKEN2, "") ?: ""
        set(value) = prefs.edit().putString(AUTHTOKEN2, value).apply()

    var userNameSave: String
        get() = prefs.getString(USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(USERNAME, value).apply()

    var userBranchSave: String
        get() = prefs.getString(USERBRANCH, "") ?: ""
        set(value) = prefs.edit().putString(USERBRANCH, value).apply()

    var isReadOnly: Boolean
        get() = prefs.getBoolean(ISREADONLY,false)
        set(value) = prefs.edit().putBoolean(ISREADONLY, value).apply()

    var isCompleteLogin: Boolean
        get() = prefs.getBoolean(ISCOMPLETELOGIN,false)
        set(value) = prefs.edit().putBoolean(ISCOMPLETELOGIN, value).apply()

}