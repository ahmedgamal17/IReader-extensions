package ireader.fastnovel

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ireader.core_api.source.Dependencies
import org.ireader.core_api.source.SourceFactory
import org.ireader.core_api.source.asJsoup
import org.ireader.core_api.source.findInstance
import org.ireader.core_api.source.model.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import tachiyomix.annotations.Extension


@Extension
abstract class FastNovel(private val deps: Dependencies) : SourceFactory(
    deps = deps,
) {

    override val lang: String
        get() = "en"
    override val baseUrl: String
        get() = "https://fastnovel.net"
    override val id: Long
        get() = 40
    override val name: String
        get() = "FastNovel"

    override fun getFilters(): FilterList = listOf(
        Filter.Title(),
    )

    override fun getCommands(): CommandList {
        return listOf(
            Command.Detail.Fetch(),
            Command.Chapter.Fetch(),
            Command.Content.Fetch(),
        )
    }

    override val exploreFetchers: List<BaseExploreFetcher>
        get() = listOf(
            BaseExploreFetcher(
                "Trending",
                endpoint = "/list/most-popular.html?page={page}",
                selector = ".film-item",
                nameSelector = ".name",
                coverSelector = ".img",
                coverAtt = "data-original",
                linkSelector = "a",
                linkAtt = "href",
                maxPage = 39,
                addBaseUrlToLink = true
            ),
            BaseExploreFetcher(
                "Search",
                endpoint = "/search/{query}",
                selector = ".film-item",
                nameSelector = "div.title > p.name",
                coverSelector = ".img",
                coverAtt = "data-original",
                linkSelector = "a",
                linkAtt = "href",
                addBaseUrlToLink = true,
                type = SourceFactory.Type.Search
            ),

            )

    override val detailFetcher: Detail
        get() = SourceFactory.Detail(
            nameSelector = "h1",
            coverSelector = ".book-cover",
            coverAtt = "data-original",
            authorBookSelector = "li > label",
            categorySelector = "div.film-info > ul > li:nth-child(2) > a",
            descriptionSelector = "div.film-content > p",
            statusSelector = "li",
        )

    override val chapterFetcher: Chapters
        get() = SourceFactory.Chapters(
            selector = ".chapter",
            nameSelector = ".chapter",
            linkSelector = ".chapter",
            linkAtt = "href",
            addBaseUrlToLink = true
            )

    override val contentFetcher: Content
        get() = SourceFactory.Content(
            pageTitleSelector = ".episode-name",
            pageContentSelector = "#chapter-body p",
        )

}