package pw.vintr.music.ui.base

interface BaseScreenState<T> {

    class Loading<T> : BaseScreenState<T>

    class Error<T> : BaseScreenState<T>

    data class Loaded<T>(val data: T) : BaseScreenState<T>
}
