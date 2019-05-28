package com.example.wan.State

import androidx.annotation.StringRes

data class State(var code: StateType, var msg: String = "", @StringRes var tip: Int = 0)