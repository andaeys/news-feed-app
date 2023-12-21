package andaeys.io.newsapp.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun convertToTimeAgo(publishedAt: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val publishedDateTime = LocalDateTime.parse(publishedAt, formatter)
    val currentDateTime = LocalDateTime.now()

    val duration = Duration.between(publishedDateTime, currentDateTime)

    return when {
        duration.toMinutes() < 1 -> "Just now"
        duration.toHours() < 1 -> "${duration.toMinutes()} mins ago"
        duration.toDays() < 2 -> "${duration.toHours()} hours ago"
        else -> "${duration.toDays()} days ago"
    }
}
