package pw.vintr.music.domain.player.model

import pw.vintr.music.domain.library.model.track.TrackModel

data class PlayerStateHolderModel(
    val session: PlayerSessionModel = PlayerSessionModel.Empty,
    val currentTrack: TrackModel? = null,
    val status: PlayerStatusModel = PlayerStatusModel.IDLE,
)
