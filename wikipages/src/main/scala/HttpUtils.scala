import scalaj.http.{HttpRequest, Http}

trait HttpUtils {
  def parse(url: String): HttpRequest
}

object HttpUtils extends HttpUtils {
  def parse(url: String): HttpRequest = Http(url)
}