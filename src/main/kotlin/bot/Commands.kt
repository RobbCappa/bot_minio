package bot

enum class Commands(
    val description: String
) {
    START("Приветствие"),
    SET_BUCKET("Задать имя бакета"),
    INFO("Информация о бакете"),
    IMAGES_ALL("Все изображения"),
    IMAGES_DIRECTORY("Изображения из директории"),
    IMAGES_INDEX("Изображения начиная с индекса"),
    CANCEL("Отменить отправку изображений")
}

fun Commands.asTgCommend(): String {
    return "/${name.lowercase()}"
}