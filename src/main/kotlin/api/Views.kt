package webindexer.api

import org.apache.lucene.document.Document
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson.asJsonObject
import webindexer.lucene.constants.Fields

object Views {
    fun search(req: Request): Response {
        val term: String = req.bodyString().asJsonObject()["term"].asText()
        val foundDocs: List<Document> = Helpers.search(term)

        val response: Map<String, Any> = mapOf(
            "count" to foundDocs.size,
            "links" to foundDocs.map { it.getField(Fields.URL) }.toTypedArray(),
        )

        return Response(Status.OK).body(response.asJsonObject().toString())
    }

    fun startCrawling(req: Request): Response {
        val link: String = req.bodyString().asJsonObject()["link"].asText()
        val searchDepth: Int = try {
            req.bodyString().asJsonObject()["search_depth"].asText().toInt()
        } catch (e: Exception) {
            2
        }
        val firstNLink: Int = try {
            req.bodyString().asJsonObject()["first_n_link"].asText().toInt()
        } catch (e: Exception) {
            2
        }

        Helpers.startCrawling(link, searchDepth, firstNLink)

        return Response(Status.OK)
    }
}