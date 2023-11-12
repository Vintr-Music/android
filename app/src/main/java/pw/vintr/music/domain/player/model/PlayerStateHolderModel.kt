package pw.vintr.music.domain.player.model

import pw.vintr.music.domain.library.model.track.TrackModel

data class PlayerStateHolderModel(
    val session: PlayerSessionModel,
    val currentTrack: TrackModel?,
    val status: PlayerStatusModel,
)
