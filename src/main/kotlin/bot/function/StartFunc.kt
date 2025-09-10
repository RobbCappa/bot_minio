package bot.function

import bot.message.MESSAGE_START
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

fun onStart(messenger: Messenger, msg: Message){
    messenger.deleteMessagesByType(
        msg,
        MessageType.USER,
        MessageType.START,
        MessageType.BUCKET,
        MessageType.ERROR
    )
    messenger.put(MessageType.USER, msg)

    messenger.reply(msg, MESSAGE_START, MessageType.START)
}