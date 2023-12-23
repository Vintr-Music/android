package pw.vintr.music.domain.visualizer.model

data class VisualizerState(
    val bytes: List<Byte> = listOf(),
    val enabled: Boolean = false,
)