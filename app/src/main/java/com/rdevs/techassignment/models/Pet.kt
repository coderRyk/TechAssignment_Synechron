import kotlinx.serialization.Serializable


@Serializable
data class Pet(
    val image_url: String?,
    val title: String?,
    val content_url: String?,
    val date_added: String?
) : java.io.Serializable