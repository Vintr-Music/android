package pw.vintr.music.domain.player.model.state

import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.config.PlayerRepeatMode
import pw.vintr.music.domain.player.model.config.PlayerShuffleMode
import pw.vintr.music.domain.player.model.session.PlayerSessionModel

data class PlayerStateHolderModel(
    val session: PlayerSessionModel = PlayerSessionModel.Empty,
    val currentTrack: TrackModel? = null,
    val status: PlayerStatusModel = PlayerStatusModel.IDLE,
    val repeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
    val shuffleMode: PlayerShuffleMode = PlayerShuffleMode.OFF
) {
    val currentTrackIndex = session.tracks.indexOf(currentTrack)
}
