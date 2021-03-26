package com.buntupana.tv_application.core

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

fun Fragment.setToolbar(toolbar: Toolbar) {
    val appBarConfiguration = AppBarConfiguration(findNavController().graph)
    toolbar.setupWithNavController(findNavController(), appBarConfiguration)
}