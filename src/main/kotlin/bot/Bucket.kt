package bot

import bot.message.ERROR_NO_BUCKET
import bot.message.MessageType
import bot.message.Messenger
import com.elbekD.bot.types.Message
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

private val BASE_URL = env["BASE_URL"]

class Bucket {
    private val buckets = mutableMapOf<Long, String>()

    fun addBucket(msg: Message, bucket: String) {
        buckets[msg.chat.id] = bucket
    }

    fun getBucketName(msg: Message) = buckets[msg.chat.id]

    fun validateBucket(msg: Message, messenger: Messenger): Boolean {
        if (getBucketName(msg) == null) {
            messenger.reply(msg, ERROR_NO_BUCKET, MessageType.ERROR)
            return false
        }
        return true
    }

    suspend fun getImagesUrl(
        prefix: String = "",
        bucketName: String?,
    ): List<String> {
        val client = HttpClient(Java)
        val xml = client.get(BASE_URL.format(bucketName)).bodyAsText()
        val doc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml.byteInputStream())
        val nodes = doc.getElementsByTagName("Key")
        return (0 until nodes.length).mapNotNull { nodes.item(it)?.textContent?.trim() }
            .filter {
                it.startsWith(prefix) && it.matches(
                    Regex(
                        ".*\\.(png|jpg|jpeg|webp)\$",
                        RegexOption.IGNORE_CASE
                    )
                )
            }
            .map { BASE_URL.format(bucketName) + it }
    }

    fun getExtractDirectoriesFromUrls(
        urls: List<String>,
        bucketName: String?,
    ): List<String> {
        val prefix = BASE_URL.format(bucketName)
        return urls.mapNotNull {
            val relative = it.removePrefix(prefix).substringBeforeLast('/', "")
            if (relative.isEmpty()) null else relative
        }.toSet().sorted()
    }
}