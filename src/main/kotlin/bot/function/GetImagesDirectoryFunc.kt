package bot.function

import bot.Bucket
import bot.message.AwaitingInputType
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

fun onGetImagesDirectory(messenger: Messenger, bucket: Bucket, msg: Message) {
    messenger.deleteMessagesByType(
        msg,
        MessageType.USER,
        MessageType.IMAGES,
        MessageType.BUCKET,
        MessageType.ERROR
    )
    messenger.put(MessageType.USER, msg)
    if (!bucket.validateBucket(msg, messenger)) return

    messenger.reply(
        msg,
        "\uD83D\uDCC1 Введи название директории (если есть) относительно \n${bucket.getBucketName(msg)}/",
        MessageType.IMAGES
    )
    messenger.awaitingInput[msg.chat.id] = AwaitingInputType.DIRECTORY
}