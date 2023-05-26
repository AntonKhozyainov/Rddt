package ru.khozyainov.rddt.model

sealed class UiModelPost{
    data class TextPost(val id: String): UiModelPost()
}