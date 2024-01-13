package webindexer.lucene.index

import webindexer.lucene.constants.indexDirectory
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.Term
import org.apache.lucene.search.*
import webindexer.lucene.constants.Settings
import java.io.Closeable

class Searcher : Closeable {
    private val reader: DirectoryReader = DirectoryReader.open(indexDirectory)
    private val searcher: IndexSearcher = IndexSearcher(reader)

    fun search(term: String): List<Document> {
        val topDocs: TopDocs = searcher.search(FuzzyQuery(Term(term)), Settings.MAX_SEARCH_RESULT)
        val scoreDocs: Array<ScoreDoc> = topDocs.scoreDocs
        val hits: TotalHits? = topDocs.totalHits

        val foundDocs = mutableListOf<Document>()

        for (sc in scoreDocs) {
            val docID: Int = sc.doc

            @Suppress("DEPRECATION") foundDocs.add(searcher.doc(docID))
        }

        return foundDocs
    }

    override fun close() {
        // Make sure to close the reader
        reader.close()
    }
}