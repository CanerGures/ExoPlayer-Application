package com.example.exoplayer.util

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private val ns: String? = null

data class GroupList(val DASH: String?, val Thumbnail: String?)
class StackOverflowXmlParser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<*> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<GroupList> {
        val entries = mutableListOf<GroupList>()

        parser.require(XmlPullParser.START_TAG, ns, "GroupList")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            entries.add(readEntry(parser))

        }
        return entries
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): GroupList {
        parser.require(XmlPullParser.START_TAG, ns, "DASH")
        var DASH: String? = null
        var Thumbnail: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "DASH" -> DASH = readDASH(parser)
                "Thumbnail" -> Thumbnail = readThumbnail(parser)
                else -> skip(parser)
            }
        }
        return GroupList(DASH, Thumbnail)
    }

    // Processes title tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDASH(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "DASH")
        val tag = parser.name

        if (tag == "DASH") {
            val DRM = parser.getAttributeValue(null, "DRM")
            val Name = parser.getAttributeValue(null, "Name")
            link = parser.getAttributeValue(null, "StreamLink")
            parser.nextTag()

        }
        parser.require(XmlPullParser.END_TAG, ns, "DASH")
        return link
    }

    // Processes link tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readThumbnail(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "Thumbnail")
        val tag = parser.name

        if (tag == "Thumbnail") {
            val DRM = parser.getAttributeValue(null, "DRM")
            val Name = parser.getAttributeValue(null, "Name")
            link = parser.getAttributeValue(null, "StreamLink")
            parser.nextTag()

        }
        parser.require(XmlPullParser.END_TAG, ns, "Thumbnail")
        return link
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }


    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            // Starts the query
            connect()
            inputStream
        }
    }
}