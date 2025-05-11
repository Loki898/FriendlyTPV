package crr.project.crirodrui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform