package webindexer.crawler

import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.Doc
import it.skrape.selects.eachHref
import it.skrape.selects.html5.*
import org.jetbrains.annotations.TestOnly

data class CrawlOutput(
    val url: String,
    val title: String,
    val content: String,
    val relatedLinks: List<String>?,
    val infoBox: String?,
)

class Crawler(
    private val startUrl: String,
    private val searchDepth: Int = 1, // crawl `startUrl` and its subpages
    private val firstNLink: Int = -1, // only use first found subpages. "-1" means use all of theme
) {
    private var currentDepth: Int = -1
    private var endOfCrawl: Boolean = false
    private val base: String = "https://wikipedia.org"

    // FIXME: the `crawlingQueue` saves each page URLs-list as a new depth
    private val crawlingQueue: MutableList<MutableList<String>> = mutableListOf()
    private val crawledUrls: MutableList<String> = mutableListOf()

    fun isFinished(): Boolean = this.endOfCrawl

    fun start() {
        crawlingQueue.add(0, mutableListOf(this.startUrl))
    }

    private fun nextUrl(): String? {
        if (this.crawlingQueue.isEmpty())
            return null

        if (this.crawlingQueue.last().isEmpty())
            this.crawlingQueue.removeLast()

        if (this.crawlingQueue.isEmpty())
            return null

        return this.crawlingQueue.last().removeLast()
    }

    fun next(): CrawlOutput? {
        var url: String? = nextUrl()
        while (this.crawledUrls.contains(url))
            url = nextUrl()

        if (url == null) {
            this.endOfCrawl = true
            return null
        }

        val crawlOutput = crawl(url)
        // Add url to crawledUrl to not crawl again
        this.crawledUrls.add(url)
        // Update current depth
        this.updateCurrentDepth()

        if (this.currentDepth < this.searchDepth)
            if (!crawlOutput.relatedLinks.isNullOrEmpty())
                this.crawlingQueue.add(
                    0,
                    crawlOutput.relatedLinks.slice(IntRange(0, this.firstNLink)).toMutableList(),
                )

        return crawlOutput
    }

    private fun updateCurrentDepth() {
        if (this.crawlingQueue.last().isEmpty())
            this.currentDepth++
    }

    private fun crawl(pageUrl: String): CrawlOutput {
        val doc = fetchDoc(pageUrl = pageUrl)

        return CrawlOutput(
            url = pageUrl,
            title = parseTitle(doc),
            content = parseContent(doc),
            relatedLinks = parseRelatedLinks(doc),
            infoBox = parseInfoBox(doc),
        )
    }

    private fun fetchDoc(pageUrl: String): Doc {
        return skrape(HttpFetcher) {
            request {
                url = pageUrl
                userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
            }

            response<Doc> { htmlDocument(responseBody) }
        }
    }

    private fun parseTitle(doc: Doc): String {
        return doc.header {
            withClass = "mw-body-header.vector-page-titlebar"
            h1 {
                withId = "firstHeading"
                findFirst {
                    this.text
                }
            }
        }
    }

    private fun parseRelatedLinks(doc: Doc): List<String>? {
        var out: List<String>? = null
        try {
            doc.div {
                withId = "bodyContent"
                findFirst {
                    this.findAll {
                        // Extract links to other wikipedia pages
                        out = eachHref.filter {
                            // Filter Wiki links
                            val isWiki = it.startsWith("/wiki/")
                            // Filter links to Portal, Contents, ... . (Wikipedia's its self pages)
                            val isForWiki = it.split(":").size == 2

                            return@filter isWiki && !isForWiki
                        }.map {
                            "${this@Crawler.base}${it}"
                        }.distinct()
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }
        return out
    }

    private fun parseInfoBox(doc: Doc): String? {
        return try {
            doc.table {
                withClass = "infobox"
                tr {
                    findAll {
                        this.map {
                            it.text
                        }
                    }
                }
            }.joinToString(" ")
        } catch (e: Exception) {
            null
        }
    }

    private fun parseContent(doc: Doc): String {
        return doc.div {
            withId = "bodyContent"
            findFirst {
                this.text
            }
        }
    }
}

@TestOnly
fun main() {
    val crawler = Crawler(
        "https://wikipedia.org/wiki/Linux",
        2,
        2,
    )
    crawler.start()

    while (!crawler.isFinished()) {
        val doc = crawler.next()
        println(doc?.content)
        println(doc?.infoBox)
        println(doc?.title)
        println(doc?.url)
    }
}