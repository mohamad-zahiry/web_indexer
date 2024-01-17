package webindexer.api

import org.apache.lucene.document.Document
import webindexer.crawler.CrawlOutput
import webindexer.crawler.Crawler
import webindexer.lucene.constants.Fields
import webindexer.lucene.index.Searcher
import webindexer.lucene.index.Writer
import webindexer.lucene.models.DocumentModel

object Helpers {
    fun startCrawling(startUrl: String, searchDepth: Int = 2, firstNLink: Int = 2) {
        val writer: Writer = Writer()
        val crawler: Crawler = Crawler(startUrl, searchDepth, firstNLink)

        crawler.start()

        var output: CrawlOutput
        var docModel: DocumentModel
        try {
            while (!crawler.isFinished()) {
                output = crawler.next() ?: continue
                docModel = DocumentModel(
                    url = output.url,
                    title = output.title,
                    content = output.content,
                    infoBox = output.infoBox ?: "",
                )

                writer.addDoc(docModel.toLuceneDocument())
            }
        } catch (e: Exception) {
            println(e)
        } finally {
            // !!! NOTE: writer `close` method must be called in any situation
            writer.close()
        }

        writer.close() // It's better to close `writer` explicitly
    }

    fun search(term: String, fields: Array<String>?): List<String> {
        val searcher: Searcher = Searcher()

        when (fields) {
            null -> searcher.search(term) // `full-text` search
            else -> searcher.search(term, fields)
        }

        return searcher.foundDocs().map { it.getField(Fields.URL).stringValue() }
    }
}