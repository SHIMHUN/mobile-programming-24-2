package com.example.todolist.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

object NavigationUtil {

    fun Fragment.navigate(action: Int) {
        this.findNavController().navigate(action)
    }

    fun Fragment.navigateWithArgs(action: Int, args: Bundle) {
        this.findNavController().navigate(action, args)
    }

    fun Fragment.popBackStack() {
        findNavController().popBackStack()
    }
}