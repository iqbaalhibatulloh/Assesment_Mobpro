package org.d3if0110.miniproject.navigation

import org.d3if0110.miniproject.ui.screen.KEY_ID_NOTE

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_NOTE}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}