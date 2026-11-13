package com.example.exp_03_androidwidgets

import androidx.annotation.DrawableRes

internal enum class Animal(val label: String, @get:DrawableRes val photo: Int) {
    LION("Lion", R.drawable.lion),
    TIGER("Tiger", R.drawable.tiger),
    MONKEY("Monkey", R.drawable.monkey),
    DOG("Dog", R.drawable.dog),
    CAT("Cat", R.drawable.cat),
    ELEPHANT("Elephant", R.drawable.elephant)
}
