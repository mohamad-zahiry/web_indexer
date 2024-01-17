package webindexer.lucene.index

import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TopDocs
import webindexer.lucene.constants.Fields
import webindexer.lucene.constants.Settings
import webindexer.lucene.constants.analyzer
import webindexer.lucene.constants.indexDirectory
import java.io.Closeable

class Searcher : Closeable {
    private val reader: DirectoryReader = DirectoryReader.open(indexDirectory)
    private val searcher: IndexSearcher = IndexSearcher(reader.context)
    private var _foundDocs: MutableList<Document> = mutableListOf()

    fun search(term: String) {
        val query = MultiFieldQueryParser(Fields.allFields(), analyzer)
            .parse(term)

        val topDocs: TopDocs = searcher.search(
            query,
            Settings.MAX_SEARCH_RESULT,
        )

        for (sc in topDocs.scoreDocs)
            @Suppress("DEPRECATION")
            _foundDocs.add(searcher.doc(sc.doc))
    }

    fun search(term: String, fields: Array<String>) {
        val query = MultiFieldQueryParser(fields, analyzer)
            .parse(term)

        val topDocs: TopDocs = searcher.search(
            query,
            Settings.MAX_SEARCH_RESULT,
        )

        for (sc in topDocs.scoreDocs)
            @Suppress("DEPRECATION")
            _foundDocs.add(searcher.doc(sc.doc))
    }

    fun foundDocs(): List<Document> = _foundDocs

    override fun close() {
        // Make sure to close the reader
        reader.close()
    }
}