package pw.vintr.music.data.player.repository

import pw.vintr.music.data.player.source.PlayerPreferencesDataStore

class PlayerConfigRepository(
    private val preferencesDataSource: PlayerPreferencesDataStore
) {

    fun setRepeatMode(repeatMode: Int) {
        preferencesDataSource.setRepeatMode(repeatMode)
    }

    fun getRepeatMode() = preferencesDataSource.getRepeatMode()

    fun setShuffleMode(shuffleMode: Int) {
        preferencesDataSource.setShuffleMode(shuffleMode)
    }

    fun getShuffleMode() = preferencesDataSource.getShuffleMode()
}
