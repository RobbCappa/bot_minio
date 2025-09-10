package bot

import bot.Commands.CANCEL
import bot.Commands.IMAGES_ALL
import bot.Commands.IMAGES_DIRECTORY
import bot.Commands.IMAGES_INDEX
import bot.Commands.INFO
import bot.Commands.SET_BUCKET
import bot.Commands.START
import bot.function.onCancel
import bot.function.onGetImagesFromIndex
import bot.function.onGetAllImages
import bot.function.onGetImagesDirectory
import bot.function.onInfo
import bot.function.onNoCommandMessage
import bot.function.onSetBucket
import bot.function.onStart
import com.elbekD.bot.Bot
import com.elbekD.bot.types.BotCommand
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import bot.message.AwaitingInputType
import bot.message.ERROR_BUCKET_IS_INCORRECT
import bot.message.ERROR_IMAGE_INDEX
import bot.message.ERROR_INDEX_LESS_ZERO
import bot.message.ERROR_NAME_BUCKET
import bot.message.MessageType
import bot.message.Messenger

val env = dotenv()
private val TOKEN = env["TOKEN"]

fun main() = runBlocking {
    val bot = Bot.createPolling("minio_img_bot", TOKEN)
    val messenger = Messenger(bot)
    val bucket = Bucket()

    bot.setMyCommands(
        Commands.entries.map { command ->
            BotCommand("/${command.name.lowercase()}", command.description)
        }
    )

    bot.onCommand(START.asTgCommend()) { msg, _ ->
        onStart(messenger, msg)
    }

    bot.onCommand(CANCEL.asTgCommend()) { msg, _ ->
        onCancel(messenger, msg)
    }

    bot.onCommand(SET_BUCKET.asTgCommend()) { msg, _ ->
        onSetBucket(messenger, msg)
    }

    bot.onCommand(INFO.asTgCommend()) { msg, _ ->
        onInfo(messenger, bucket, msg)
    }

    bot.onCommand(IMAGES_ALL.asTgCommend()) { msg, _ ->
        onGetAllImages(messenger, bucket, msg)
    }

    bot.onCommand(IMAGES_INDEX.asTgCommend()) { msg, _ ->
        onGetImagesFromIndex(messenger, bucket, msg)
    }

    bot.onCommand(IMAGES_DIRECTORY.asTgCommend()) { msg, _ ->
        onGetImagesDirectory(messenger, bucket, msg)
    }

    bot.onMessage { msg ->
        onNoCommandMessage(messenger, bucket, msg)
    }

    bot.start()
}