package webindexer.lucene.constants

object Fields {
    const val URL: String = "url"
    const val TITLE: String = "title"
    const val CONTENT: String = "content"
    const val INFO_BOX: String = "info_box"

    fun allFields(): Array<String> = arrayOf(URL, TITLE, CONTENT, INFO_BOX)
}