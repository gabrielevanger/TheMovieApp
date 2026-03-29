package com.example.themovieapp.model

import androidx.annotation.DrawableRes
import com.example.themovieapp.R

data class Movie(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val synopsis: String,
    val rating: Float,
    @DrawableRes val posterResId: Int,
    @DrawableRes val galleryResIds: List<Int>,
) {
    fun synopsisPreview(maxChars: Int = 140): String {
        if (synopsis.length <= maxChars) return synopsis
        return synopsis.take(maxChars).trimEnd() + "…"
    }

    /** Sinopse curta para o card da lista (Data Binding). */
    val synopsisForListCard: String get() = synopsisPreview(140)

    fun ratingPercent(): Int = (rating / 10f * 100f).toInt().coerceIn(0, 100)

    fun titleWithYear(): String = "$title ($releaseYear)"

    companion object {
        /**
         * Pôsteres baixados do catálogo TMDB (themoviedb.org) e incluídos em res/drawable-nodpi/
         * para uso offline no app.
         */
        val sampleList: List<Movie> = listOf(
            Movie(
                id = "1",
                title = "Interestelar",
                releaseYear = 2014,
                synopsis = "Em um futuro próximo, a Terra está à beira do colapso ambiental. " +
                    "Um grupo de exploradores espaciais atravessa um buraco de minhoca recém-descoberto " +
                    "em busca de um novo lar para a humanidade, enfrentando o tempo e o desconhecido.",
                rating = 8.6f,
                posterResId = R.drawable.poster_interstellar,
                galleryResIds = listOf(
                    R.drawable.poster_interstellar_wide,
                    R.drawable.poster_interstellar,
                    R.drawable.poster_interstellar_wide,
                ),
            ),
            Movie(
                id = "2",
                title = "A Origem",
                releaseYear = 2010,
                synopsis = "Dom Cobb é um ladrão especializado em extrair segredos do subconsciente " +
                    "durante o sonho. Oferecem-lhe uma chance de redenção: plantar uma ideia na mente " +
                    "de alguém — o crime perfeito, se não fosse a dúvida sobre o que é real.",
                rating = 8.8f,
                posterResId = R.drawable.poster_inception,
                galleryResIds = listOf(
                    R.drawable.poster_inception_wide,
                    R.drawable.poster_inception,
                    R.drawable.poster_inception_wide,
                ),
            ),
            Movie(
                id = "3",
                title = "Blade Runner 2049",
                releaseYear = 2017,
                synopsis = "Trinta anos após os eventos do primeiro filme, um novo blade runner, K, " +
                    "descobre um segredo enterrado que pode mergulhar o que resta da sociedade no caos. " +
                    "Sua investigação o leva a procurar Rick Deckard, desaparecido há décadas.",
                rating = 8.0f,
                posterResId = R.drawable.poster_blade_runner_2049,
                galleryResIds = listOf(
                    R.drawable.poster_blade_runner_2049_wide,
                    R.drawable.poster_blade_runner_2049,
                    R.drawable.poster_blade_runner_2049_wide,
                ),
            ),
            Movie(
                id = "4",
                title = "Duna",
                releaseYear = 2021,
                synopsis = "Paul Atreides, jovem nobre, viaja para o planeta desértico Arrakis, " +
                    "fonte da substância mais valiosa do universo. Traições e visões forçam-no a escolher " +
                    "entre o destino da sua família e o de todo um povo.",
                rating = 8.0f,
                posterResId = R.drawable.poster_dune_2021,
                galleryResIds = listOf(
                    R.drawable.poster_dune_2021_wide,
                    R.drawable.poster_dune_2021,
                    R.drawable.poster_dune_2021_wide,
                ),
            ),
        )
    }
}
