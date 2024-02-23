package pw.vintr.music.ui.navigation.navArgs.parcelableList

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableListWrapper<T: Parcelable>(
    val content: List<T>
) : Parcelable
