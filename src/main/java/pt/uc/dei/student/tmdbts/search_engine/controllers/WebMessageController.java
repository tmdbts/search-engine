package pt.uc.dei.student.tmdbts.search_engine.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebMessageController {

    @MessageMapping("/webmessage")
    @SendTo("/Topic/webmessages")
    public WebMessage webMessages(WebMessage webMessage) throws InterruptedException {
        System.out.println("Message: " + webMessage);
        Thread.sleep(1000);
        return new WebMessage(HtmlUtils.htmlEscape(webMessage.toString()));
    }
}
