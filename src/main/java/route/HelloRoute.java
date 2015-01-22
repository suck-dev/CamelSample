package route;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class HelloRoute extends SpringRouteBuilder {

  @Override
  public void configure() throws Exception {
//    from("timer:start?period=3s")
//      .to("log:end");

    // エラー処理
    onException(Exception.class)
      .handled(true)
      .to("log:error")
      .removeHeaders("*")
      .setHeader(Exchange.HTTP_RESPONSE_CODE).constant(500);

    // 通常処理
    from("servlet:///list")
      .to("log:in")
      .to("sql:SELECT * FROM myitems WHERE user_id = :#id")
      .marshal().json(JsonLibrary.Jackson)
      .removeHeaders("*")
      .to("log:out");
  }

}
