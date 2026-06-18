package com.kopo.hkcode.annot;

import org.springframework.stereotype.Component;

@Component("stringPrinter")
public class StringPrinter implements Printer {

   private String message; // 값 저장

   @Override
   public void print(String message) {
      this.message = message; // 콘솔 대신 필드에 저장
   }

   public String getMessage() { // Controller에서 꺼낼 수 있게
      return message;
   }

}