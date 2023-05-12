package ru.khozyainov.rddt.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingItem(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int
)
