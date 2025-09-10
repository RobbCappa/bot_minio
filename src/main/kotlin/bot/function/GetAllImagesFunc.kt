package bot.function

import bot.Bucket
import bot.message.ERROR_BUCKET_IS_INCORRECT
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

suspend fun onGetAllImages(messenger: Messenger, bucket: Bucket, msg: Message) {
    messenger.deleteMessagesByType(
        msg,
        MessageType.USER,
        MessageType.IMAGES,
        MessageType.BUCKET,
        MessageType.ERROR
    )
    messenger.put(MessageType.USER, msg)
    if (!bucket.validateBucket(msg, messenger)) return

    val images = bucket.getImagesUrl(bucketName = bucket.getBucketName(msg))
    if (images.isEmpty()) messenger.reply(msg, ERROR_BUCKET_IS_INCORRECT, MessageType.IMAGES)
    else {
        messenger.reply(msg, "В бакете ${bucket.getBucketName(msg)} ${images.size} картинок.", MessageType.IMAGES)
        messenger.sendImages(msg, images, MessageType.IMAGES)
    }
}