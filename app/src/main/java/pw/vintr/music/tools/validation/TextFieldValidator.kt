package pw.vintr.music.tools.validation

object TextFieldValidator {

    private const val PLAYLIST_NAME_MAX_LENGTH = 30
    private const val PLAYLIST_DESCRIPTION_MAX_LENGTH = 500

    fun validatePlaylistNameInput(value: String, approvedAction: (String) -> Unit) {
        if (value.length <= PLAYLIST_NAME_MAX_LENGTH) {
            approvedAction(value)
        }
    }

    fun validatePlaylistDescriptionInput(value: String, approvedAction: (String) -> Unit) {
        if (value.length <= PLAYLIST_DESCRIPTION_MAX_LENGTH) {
            approvedAction(value)
        }
    }
}
