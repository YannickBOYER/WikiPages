import scopt.OParser
import scalaj.http.Http
import play.api.libs.json.{Json, JsArray}

case class Config(limit: Int = 10, keyword: String = "")

case class WikiPage(title: String, words: Int)

object Main extends App {
  parseArguments(args) match {
    case Some(config) => run(config)
    case _            => println("Unable to parse arguments")
  }

  def parseArguments(args: Array[String]): Option[Config] = {
    val builder = OParser.builder[Config]
    val parser = {
      import builder._
      OParser.sequence(
        programName("WikiStats"),
        opt[Int]('l', "limit")
          .action((limit: Int, config: Config) => config.copy(limit = limit))
          .text("limit the number of results to show from Wikipedia"),
        opt[String]('k', "keyword")
          .required
          .action((keyword: String, config: Config) => config.copy(keyword = keyword))
          .text("keyword to search for in Wikipedia"),

      )
    }

    OParser.parse(parser, args, Config())
  }

  def run(config: Config): Unit = {
    println(config)
    val url = formatUrl(config.keyword, config.limit)
    val httpUtils = HttpUtils
    getPages(url, httpUtils) match {
      case Right(body) => {
        val pages = parseJson(body)
        println(s"${pages.length} pages found")
        pages.foreach { page =>
          println(s"${page.title} - ${page.words} words")
        }
        println(s"Total words: ${totalWords(pages)}")
        println(s"Average words per page: ${totalWords(pages) / pages.length}")
      }
      case Left(code)  => println(s"Error: $code")
    }

  }

    def formatUrl(keyword: String, limit: Int): String = {
    // string interpolation (I will use it several times in this code)
    s"https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=$keyword&srlimit=$limit"
  }

  def getPages(url: String, httpUtils: HttpUtils): Either[Int, String] = {
    val result = httpUtils.parse(url).asString
    if (result.code == 200) Right(result.body)
    else Left(result.code)
  }

  def parseJson(rawJson: String): Seq[WikiPage] = {
    val json = Json.parse(rawJson)
    val pages = (json \ "query" \ "search").as[JsArray].value
    pages.map { page =>
      val title = (page \ "title").as[String]
      val words = (page \ "wordcount").as[Int]
      WikiPage(title, words)
    }
  }

  def totalWords(pages: Seq[WikiPage]): Int = {
    pages.foldLeft(0)((total, currentPage) => total + currentPage.words)
  }
}

