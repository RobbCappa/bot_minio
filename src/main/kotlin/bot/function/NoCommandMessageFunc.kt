package bot.function

import bot.Bucket
import bot.message.AwaitingInputType
import bot.message.ERROR_BUCKET_IS_INCORRECT
import bot.message.ERROR_IMAGE_INDEX
import bot.message.ERROR_INDEX_LESS_ZERO
import bot.message.ERROR_NAME_BUCKET
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

suspend fun onNoCommandMessage(messenger: Messenger, bucket: Bucket, msg: Message) {
    listOf(
        MessageType.USER,
        MessageType.ERROR
    ).forEach { messenger.deleteMessageByType(it, msg) }
    messenger.put(MessageType.USER, msg)
    val text = msg.text?.trim() ?: return

    when (messenger.awaitingInput.remove(msg.chat.id)) {
        AwaitingInputType.BUCKET -> {
            if (text.isEmpty()) messenger.reply(msg, ERROR_NAME_BUCKET, MessageType.ERROR)
            else {
                bucket.addBucket(msg, text)
                messenger.reply(msg, "✅ Имя бакета обновлено:\n$text", MessageType.BUCKET)
            }
        }

        AwaitingInputType.DIRECTORY -> {
            messenger.deleteMessagesByType(
                msg,
                MessageType.IMAGES,
                MessageType.BUCKET,
                MessageType.ERROR
            )

            val images = bucket.getImagesUrl(text, bucket.getBucketName(msg))
            if (images.isEmpty()) {
                messenger.reply(msg, "📂 В папке $text ничего не найдено", MessageType.IMAGES)
            } else {
                messenger.reply(msg, "📂 В папке $text найдено ${images.size} изображений", MessageType.IMAGES)
                messenger.sendImages(msg, images, MessageType.IMAGES)
            }
        }

        AwaitingInputType.POSITION -> {
            messenger.deleteMessagesByType(
                msg,
                MessageType.IMAGES,
                MessageType.BUCKET,
                MessageType.ERROR
            )

            val index = msg.text?.trim()?.toIntOrNull()

            if (index == null || index <= 0) messenger.reply(msg, ERROR_INDEX_LESS_ZERO, MessageType.ERROR)
            else {
                val images = bucket.getImagesUrl(bucketName = bucket.getBucketName(msg))
                if (images.isEmpty()) messenger.reply(msg, ERROR_BUCKET_IS_INCORRECT, MessageType.ERROR)
                else if (images.size < index) messenger.reply(msg, ERROR_IMAGE_INDEX, MessageType.ERROR)
                else {
                    messenger.sendImages(
                        msg, images.subList(
                            fromIndex = index,
                            toIndex = images.size
                        ), MessageType.IMAGES
                    )
                }
            }
        }

        null -> messenger.deleteMessage(msg.chat.id, msg.message_id)
    }
}