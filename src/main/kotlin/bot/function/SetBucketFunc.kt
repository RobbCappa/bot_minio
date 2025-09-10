package bot.function

import bot.message.AwaitingInputType
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

fun onSetBucket(messenger: Messenger, msg: Message) {
    messenger.deleteMessagesByType(
        msg,
        MessageType.USER,
        MessageType.BUCKET,
        MessageType.ERROR
    )
    messenger.put(MessageType.USER, msg)

    messenger.reply(msg, "\uD83E\uDEA3 Пришли название бакета", MessageType.BUCKET)
    messenger.awaitingInput[msg.chat.id] = AwaitingInputType.BUCKET
}