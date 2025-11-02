package com.example.musiletra.ui

sealed class Screen {
    object List : Screen()
    object Add : Screen()
    object Edit : Screen()
    object Detail : Screen()
    object OnlineSearch : Screen()
}