import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Pet(
    val image_url: String?,
    val title: String?,
    val content_url: String?,
    val date_added: String?
) : Parcelable