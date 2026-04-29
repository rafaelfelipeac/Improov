package com.rafaelfelipeac.improov.core.ui.theme

enum class AppThemeMode {
    LIGHT,
    DARK,
    ;

    fun isDark(): Boolean = this == DARK

    companion object {
        fun from(value: String?): AppThemeMode {
            return entries.firstOrNull { it.name == value } ?: LIGHT
        }
    }
}
