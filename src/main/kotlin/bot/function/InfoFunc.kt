package bot.function

import bot.Bucket
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

suspend fun onInfo(messenger: Messenger, bucket: Bucket, msg: Message) {
    messenger.deleteMessagesByType(
        msg,
        MessageType.USER,
        MessageType.INFO,
        MessageType.BUCKET,
        MessageType.ERROR
    )
    messenger.put(MessageType.USER, msg)
    if (!bucket.validateBucket(msg, messenger)) return

    val links = bucket.getImagesUrl(bucketName = bucket.getBucketName(msg))
    val directories = bucket.getExtractDirectoriesFromUrls(links, bucket.getBucketName(msg))
    val text = buildString {
        appendLine("🪣 Текущий бакет: ${bucket.getBucketName(msg)}")
        appendLine()
        appendLine("🖼 Количество изображений: ${links.size}")
        appendLine()
        appendLine("📁 Вложенные директории:")
        if (directories.isEmpty()) appendLine("— (не найдено)") else directories.forEach { appendLine("• $it") }
    }
    messenger.reply(msg, text, MessageType.INFO)
}