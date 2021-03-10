import android.os.Parcel
import android.os.Parcelable


data class Pet(
    val image_url: String?,
    val title: String?,
    val content_url: String?,
    val date_added: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image_url)
        parcel.writeString(title)
        parcel.writeString(content_url)
        parcel.writeString(date_added)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pet> {
        override fun createFromParcel(parcel: Parcel): Pet {
            return Pet(parcel)
        }

        override fun newArray(size: Int): Array<Pet?> {
            return arrayOfNulls(size)
        }
    }
}