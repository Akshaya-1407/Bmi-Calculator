package com.example.mybmi

sealed class Screen(val route : String) {
    object MainScreen : Screen("main_screen")
    object Result : Screen("result")


}