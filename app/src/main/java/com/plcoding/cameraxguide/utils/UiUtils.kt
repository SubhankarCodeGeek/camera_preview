package com.plcoding.cameraxguide.utils

import android.content.Context
import android.widget.Toast

fun Context.makeToast(msg: String?) = Toast.makeText(
    applicationContext,
    msg,
    Toast.LENGTH_LONG
).show()