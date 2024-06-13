import scalaj.http.{HttpRequest, Http, HttpOptions, HttpConstants, HttpResponse}
import scala.util.Random
import scala.language.higherKinds

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

object MockHttpUtils extends HttpUtils {
  override def parse(url: String): HttpRequest = new HttpRequest(url, "GET", null, Seq(), Seq(), Seq(), None, "UTF-8", 4096, _.toString, false, None) {
    override def asString: HttpResponse[String] = {
      HttpResponse("""{"query": {"search": [{"title": "Test Mock", "wordcount": 10}]}}""", 200, Map.empty)
    }
  }
}

class MainSpec extends AnyFlatSpec with Matchers{
  "formatUrlTest" should "return the correct URL" in {
    // Given
    val keyword = "scala"
    val limit = 10
    // When
    val result = Main.formatUrl(keyword, limit)
    // Then
    result shouldBe "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=scala&srlimit=10"
  }

  "parseJson" should "return a list of WikiPage" in {
    // Given
    val json = """{"query": {"search": [{"title": "Scala", "wordcount": 100}, {"title": "Test", "wordcount": 200}]}}"""
    // When
    val result = Main.parseJson(json)
    // Then
    result shouldBe Seq(WikiPage("Scala", 100), WikiPage("Test", 200))
  }

  "totalWords" should "return the sum of all words" in {
    // Given
    val pages = Seq(WikiPage("Scala", 100), WikiPage("Test", 200))
    // When
    val result = Main.totalWords(pages)
    // Then
    result shouldBe 300
  }
  it should "return 0 if the list is empty" in {
    // Given
    val pages = Seq.empty
    // When
    val result = Main.totalWords(pages)
    // Then
    result shouldBe 0
  }

  "parseArguments" should "return None if the arguments are not parsable" in {
    // Given
    val args = Array("--doesNoExist", "10")
    // When
    val result = Main.parseArguments(args)
    // Then
    result shouldBe None
  }
  it should "return a Config object with the keyword" in {
    // Given
    val args = Array("--keyword", "scala")
    // When
    val result = Main.parseArguments(args)
    // Then
    result shouldBe Some(Config(keyword = "scala"))
  }
  it should "return a Config object with the keyword and the limit" in {
    // Given
    val args = Array("--keyword", "scala", "--limit", "10")
    // When
    val result = Main.parseArguments(args)
    // Then
    result shouldBe Some(Config(keyword = "scala", limit = 10))
  }

  "getPagesMock" should "return a Right if the request successes" in {
    // Given
    val url = "http://example.com"
    val mockHttpUtils = MockHttpUtils
    // When
    val result = Main.getPages(url, mockHttpUtils)
    // Then
    result.isRight shouldBe true
  }
}