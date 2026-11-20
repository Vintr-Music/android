package pw.vintr.music.core.navigation.screen

interface Screen {
    val route: String
}

fun buildRoute(
    destination: String,
    params: Map<String, String> = mapOf()
) = buildString {
    append(destination)
    params.forEach { append("?${it.key}=${it.value}") }
}

fun buildRouteTemplate(
    destination: String,
    paramKeys: List<String>
) = buildString {
    append(destination)
    paramKeys.forEach { append("?$it={$it}") }
}

abstract class ScreenWithArgs(
    destination: String,
    args: Map<String, String>
) : Screen {
    override val route: String = buildRoute(
        destination = destination,
        params = args
    )
}
