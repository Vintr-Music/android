package pw.vintr.music.ui.feature.dialog.entity

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed interface ConfirmDialogData : Parcelable {

    @Parcelize
    class Resource(
        @StringRes
        val titleRes: Int,
        @StringRes
        val messageRes: Int,
        @StringRes
        val acceptTextRes: Int? = null,
        @StringRes
        val declineTextRes: Int? = null,
    ) : ConfirmDialogData

    @Parcelize
    class Text(
        val title: String,
        val message: String,
        val acceptText: String? = null,
        val declineText: String? = null,
    ) : ConfirmDialogData
}
