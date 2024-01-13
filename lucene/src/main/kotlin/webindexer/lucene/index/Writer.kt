package webindexer.lucene.index

import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexWriter
import webindexer.lucene.constants.indexDirectory
import webindexer.lucene.constants.indexWriterConfig
import java.io.Closeable

class Writer : Closeable {
    private val writer: IndexWriter = IndexWriter(indexDirectory, indexWriterConfig)

    fun addDoc(doc: Document) {
        writer.addDocument(doc)
    }

    fun addDocs(docs: List<Document>) {
        writer.addDocuments(docs)
    }

    override fun close() {
        // Make sure to close the writer
        writer.close()
    }
}