package com.buntupana.tv_application.presentation.films

import androidx.arch.core.util.Function
import com.buntupana.tv_application.domain.entities.Film

class FilmEntityViewMapper(private val imageResource: String) : Function<Film, FilmEntityView> {
    override fun apply(input: Film): FilmEntityView {

        var durationMin: Int = (input.duration / 1000 / 60).toInt()
        val durationHours: Int = durationMin / 60
        durationMin = durationMin.rem(60)

        val categories = input.categoryList.joinToString(", ") { category -> category.name }

        return FilmEntityView(
            input.filmId,
            imageResource,
            input.title,
            input.year,
            durationHours,
            durationMin,
            categories,
            input.favourite
        )
    }
}