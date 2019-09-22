package com.meretas.itinventory.utils

import android.view.View

fun View.disable() {
    alpha = .5f
    isClickable = false
}

fun View.enable() {
    alpha = 1f
    isClickable = true
}