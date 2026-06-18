package com.kopo.hkcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kopo.hkcode.pojo.Hello;
import com.kopo.hkcode.pojo.StringPrinter;

@Controller
public class HelloController2 {

   @Autowired
   private Hello hello;

   @Autowired
   private StringPrinter stringPrinter;

   @RequestMapping(value = "/helloTest", method = RequestMethod.GET) // 웹 브라우저에서 /hello로 접속하면 실행
   public String helloTest(Model model) {

      hello.print();
      String result = stringPrinter.getMessage();
      model.addAttribute("message", result);
      return "hellopojo"; // /WEB-INF/views/hello.jsp 를 찾아감
   }

   @RequestMapping("/hello") // 웹 브라우저에서 /hello로 접속하면 실행
   public String hello(Model model) {
      model.addAttribute("message", "Hello World from Spring MVC!");
      return "hellopojo"; // /WEB-INF/views/hello.jsp 를 찾아감
   }
}
