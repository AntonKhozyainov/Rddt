package ru.khozyainov.rddt.mapper

import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.rddt.model.UiPostSortType
import java.lang.Exception

class UiPostSortTypeMapper {

    fun mapToDomain(uiPostSortType: UiPostSortType): PostSortType{
        return PostSortType(sort = uiPostSortType.toString())
    }

    fun mapToUi(postSortType: PostSortType): UiPostSortType{
        return when(postSortType.sort){
            UiPostSortType.HOT.toString() -> UiPostSortType.HOT
            UiPostSortType.NEW.toString() -> UiPostSortType.NEW
            UiPostSortType.TOP.toString() -> UiPostSortType.TOP
            UiPostSortType.CONTROVERSIAL.toString() -> UiPostSortType.CONTROVERSIAL
            UiPostSortType.RISING.toString() -> UiPostSortType.RISING
            else -> throw Exception("Incorrect sort type = ${postSortType.sort}")
        }
    }
}