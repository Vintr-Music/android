package pw.vintr.music.core.navigation.navArgs

interface NavTypeSerializer<T : Any> {

    fun toRouteString(value: T): String

    fun fromRouteString(routeStr: String): T
}
