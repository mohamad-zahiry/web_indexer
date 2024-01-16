package webindexer.lucene.models

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import webindexer.lucene.constants.Fields

data class DocumentModel(
    val url: String,
    val title: String,
    val content: String,
    val infoBox: String,
) {
    fun toLuceneDocument(): Document {
        val doc = Document()

        doc.add(StringField(Fields.URL, url, Field.Store.YES))
        doc.add(TextField(Fields.TITLE, title, Field.Store.YES))
        doc.add(TextField(Fields.CONTENT, content, Field.Store.YES))
        doc.add(TextField(Fields.INFO_BOX, infoBox, Field.Store.YES))

        return doc
    }
}