package webindexer.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson.asJsonObject

object Views {
    fun search(req: Request): Response {
        val term: String = req.bodyString().asJsonObject()["term"].asText()
        var fields: Array<String>? = req.bodyString()
            .asJsonObject()["fields"]
            ?.asIterable()
            ?.map { it.asText() }
            ?.filter { it.isNotEmpty() }
            ?.toTypedArray()

        if (fields.isNullOrEmpty())
            fields = null

        val response: Map<String, List<String>> = mapOf(
            "links" to Helpers.search(term, fields),
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