package bot.function

import bot.Bucket
import bot.message.AwaitingInputType
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

fun onGetImagesFromIndex(messenger: Messenger, bucket: Bucket, msg: Message) {
    messenger.deleteMessagesByType(
        msg,
        MessageType.USER,
        MessageType.IMAGES,
        MessageType.BUCKET,
        MessageType.ERROR
    )
    messenger.put(MessageType.USER, msg)
    if (!bucket.validateBucket(msg, messenger)) return

    messenger.reply(msg, "\uD83D\uDCE5 Введи номер, с которого начать вывод картинок", MessageType.IMAGES)
    messenger.awaitingInput[msg.chat.id] = AwaitingInputType.POSITION
}