import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class OrderServiceLoad extends Simulation {

  val httpConf = http
    .baseURL("http://host.docker.internal:9555")
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> "application/json")

  val scn = scenario("Create ramen order and accept ETA")
      .exec(http("order_ramen")
          .post("/orders")
          .body(RawFileBody("3_order.json")).asJSON
          .check(jsonPath("$").saveAs("jsonResponse"))
          .check(jsonPath("$.id").saveAs("order_id"))
          .check(status.is(200)))
      .exec(session => {
        val json:String = session.attributes.get("jsonResponse").get.asInstanceOf[String].replaceFirst("WAITING", "VALIDATED")
        session.set("json", json)
      })
      .exec(http("accept_ETA")
            .put("/orders/${order_id}")
            .body(StringBody("${json}")).asJSON
            .check(status.is(200)))
      //.pause(1)

  setUp(
    scn.inject(
      rampUsersPerSec(10) to 20 during (20 seconds) randomized,
      )
    ).protocols(httpConf)
}
