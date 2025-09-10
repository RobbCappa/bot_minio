package bot.function

import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message

fun onCancel(messenger: Messenger, msg: Message) {
    messenger.put(MessageType.USER, msg)
    messenger.needStopSendingImages[msg.chat.id] = true
}