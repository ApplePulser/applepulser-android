package com.shinythinking.applepulser_android.navigation

import androidx.navigation.NavController

inline fun <reified T : Route> NavController.navigateAndClearBackStack(route: T) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

inline fun <reified T : Route, reified PopUpTo : Route> NavController.navigateAndPopUpTo(
    route: T,
    inclusive: Boolean = false
) {
    navigate(route) {
        popUpTo<PopUpTo> {
            this.inclusive = inclusive
        }
        launchSingleTop = true
    }
}

inline fun <reified T : Route> NavController.navigateSingleTop(route: T) {
    navigate(route) {
        launchSingleTop = true
    }
}

inline fun <reified T : Route> NavController.popBackStackTo(inclusive: Boolean = false) {
    popBackStack<T>(inclusive = inclusive)
}