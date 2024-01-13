package webindexer.lucene.constants

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.FSDirectory
import kotlin.io.path.Path

val indexDirectory: FSDirectory = FSDirectory.open(Path(Settings.INDEX_DIR_PATH))
val analyzer: Analyzer = StandardAnalyzer()
val indexWriterConfig: IndexWriterConfig = IndexWriterConfig(analyzer)
