package webindexer.lucene.index

import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.PhraseQuery
import org.apache.lucene.search.TopDocs
import webindexer.lucene.constants.Fields
import webindexer.lucene.constants.Settings
import webindexer.lucene.constants.indexDirectory
import java.io.Closeable
import java.util.*

class Searcher : Closeable {
    private val reader: DirectoryReader = DirectoryReader.open(indexDirectory)
    private val searcher: IndexSearcher = IndexSearcher(reader.context)

    fun search(term: String): List<Document> {
        val normalizedTerm: Array<String> = term
            .lowercase(Locale.getDefault())
            .split(" ")
            .toTypedArray()

        val topDocs: TopDocs = searcher.search(
            PhraseQuery(Fields.TITLE, *normalizedTerm),
            Settings.MAX_SEARCH_RESULT,
        )

        val foundDocs = mutableListOf<Document>()
        for (sc in topDocs.scoreDocs)
            @Suppress("DEPRECATION")
            foundDocs.add(searcher.doc(sc.doc))

        return foundDocs
    }

    override fun close() {
        // Make sure to close the reader
        reader.close()
    }
}