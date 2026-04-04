package com.example.themovieapp.model

data class Movie(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val synopsis: String,
    val rating: Float,
    val voteCount: Int = 0,
    val posterUrl: String?,
    val backdropUrl: String?,
    val galleryImageUrls: List<String>,
) {
    fun synopsisPreview(maxChars: Int = 140): String {
        if (synopsis.length <= maxChars) return synopsis
        return synopsis.take(maxChars).trimEnd() + "…"
    }

    val synopsisForListCard: String get() = synopsisPreview(140)

    fun ratingPercent(): Int = (rating / 10f * 100f).toInt().coerceIn(0, 100)

    fun titleWithYear(): String =
        if (releaseYear > 0) "$title ($releaseYear)" else title

    fun heroImageUrl(): String? = backdropUrl ?: posterUrl
}
