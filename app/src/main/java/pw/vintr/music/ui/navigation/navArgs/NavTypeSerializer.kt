package pw.vintr.music.ui.navigation.navArgs

interface NavTypeSerializer<T: Any> {

    fun toRouteString(value: T): String

    fun fromRouteString(routeStr: String): T
}
