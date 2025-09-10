package bot.message

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutionException

class Messenger(
    private val bot: Bot
) {
    val awaitingInput = mutableMapOf<Long, AwaitingInputType>()
    val needStopSendingImages = mutableMapOf<Long, Boolean>()

    private val messageTrackers = mapOf(
        MessageType.START to mutableMapOf(),
        MessageType.INFO to mutableMapOf(),
        MessageType.BUCKET to mutableMapOf(),
        MessageType.IMAGES to mutableMapOf(),
        MessageType.USER to mutableMapOf(),
        MessageType.ERROR to mutableMapOf<Long, MutableList<Long>>()
    )

    fun reply(msg: Message, text: String, type: MessageType) {
        bot.sendMessage(msg.chat.id, text).also { put(type, it.get()) }
    }

    fun put(type: MessageType, msg: Message) {
        messageTrackers[type]?.computeIfAbsent(msg.chat.id) { mutableListOf() }?.add(msg.message_id)
    }

    fun deleteMessage(chatId: Long, messageId: Long) {
        bot.deleteMessage(chatId, messageId)
    }

    fun deleteMessageByType(type: MessageType, msg: Message) {
        messageTrackers[type]?.get(msg.chat.id)?.forEach { deleteMessage(msg.chat.id, it) }
        messageTrackers[type]?.get(msg.chat.id)?.clear()
    }

    fun deleteMessagesByType(msg: Message, vararg massageType: MessageType) {
        massageType.forEach { deleteMessageByType(it, msg) }
    }

    suspend fun sendImages(msg: Message, urls: List<String>, type: MessageType) {
        needStopSendingImages[msg.chat.id] = false
        var index = 0

        while (index < urls.size) {
            if (needStopSendingImages[msg.chat.id] == true) {
                reply(msg, "🚫 отправка изображений отменена", MessageType.ERROR)
                break
            }

            val url = urls[index]
            try {
                bot.sendPhoto(
                    chatId = msg.chat.id,
                    photo = url,
                    caption = "$url\n\n🖼 ${index + 1}/${urls.size}"
                ).also { put(type, it.get()) }

                index++
            } catch (e: ExecutionException) {
                delay(1000L)
            }
        }
    }
}