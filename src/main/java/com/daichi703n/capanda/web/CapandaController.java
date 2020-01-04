package com.daichi703n.capanda.web;

import java.security.Principal;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class CapandaController {

    @RequestMapping(method = RequestMethod.GET)
    // TODO: move to Interceptor
    public String index(Model model, Principal principal) {
        log.info("Request received.");
        String readmeUrl = "https://github.com/daichi703n/capanda/blob/master/README.md";
        RestTemplate restTemplate = new RestTemplate();
        String resultStr = restTemplate.getForObject(readmeUrl, String.class);

        String findStart = "<article ";
        String findEnd = "</article>";
        String readmeStr = resultStr
            .substring(resultStr.indexOf(findStart), resultStr.indexOf(findEnd)+findEnd.length())
            .replace("/daichi703n/capanda","https://github.com/daichi703n/capanda");

        // TODO: move to Interceptor
        if (Objects.nonNull(principal)) {
            System.out.println(principal.toString());
            String notify_message = principal.getName();
            model.addAttribute("notify_message",notify_message);
        }
        model.addAttribute("msg",readmeStr);
        return "index";
    }

    @RequestMapping(path = "test/login", method = RequestMethod.GET)
    public String testLoginGet(Model model) {
        return "test/login";
    }

    @RequestMapping(path = "test/login", method = RequestMethod.POST)
    public String testLoginPost(Model model, @RequestBody String body) {
        model.addAttribute("msg",body);
        return "test/login";
    }

    @RequestMapping(path = "test/payment", method = RequestMethod.GET)
    public String testPaymentGet(Model model) {
        return "test/payment";
    }

    @RequestMapping(path = "test/payment", method = RequestMethod.POST)
    public String testPaymentPost(Model model, @RequestBody String body) {
        model.addAttribute("msg",body);
        return "test/payment";
    }

}
