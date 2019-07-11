package com.meretas.itinventory.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    private val _prefsFileName = "prefs"
    private val _authToken = "authTokenSave"
    private val _authTokenOne = "authTokenSave_one"
    private val _authTokenTwo = "authTokenSave_two"

    private val _userName = "userName"
    private val _userBranch = "userBranch"
    private val _isReadOnly = "userReadOnly"
    private val _isCompleteLlogin = "userCompleteLogin"

    val prefs: SharedPreferences = context.getSharedPreferences(_prefsFileName, 0)

    var authTokenSave: String
        get() = prefs.getString(_authToken, "") ?: ""
        set(value) = prefs.edit().putString(_authToken, value).apply()

    var authTokenOne: String
        get() = prefs.getString(_authTokenOne, "") ?: ""
        set(value) = prefs.edit().putString(_authTokenOne, value).apply()

    var authTokenTwo: String
        get() = prefs.getString(_authTokenTwo, "") ?: ""
        set(value) = prefs.edit().putString(_authTokenTwo, value).apply()

    var userNameSave: String
        get() = prefs.getString(_userName, "") ?: ""
        set(value) = prefs.edit().putString(_userName, value).apply()

    var userBranchSave: String
        get() = prefs.getString(_userBranch, "") ?: ""
        set(value) = prefs.edit().putString(_userBranch, value).apply()

    var isReadOnly: Boolean
        get() = prefs.getBoolean(_isReadOnly,false)
        set(value) = prefs.edit().putBoolean(_isReadOnly, value).apply()

    var isCompleteLogin: Boolean
        get() = prefs.getBoolean(_isCompleteLlogin,false)
        set(value) = prefs.edit().putBoolean(_isCompleteLlogin, value).apply()

}