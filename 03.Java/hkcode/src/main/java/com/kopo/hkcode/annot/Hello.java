package com.kopo.hkcode.annot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("hello")
public class Hello {
   private String name;
   
   @Autowired
   @Qualifier("stringPrinter")
   private Printer printer;
     
   public void setName(String name) {
      this.name = name;
   }
   public void setPrinter(Printer printer) {
      this.printer = printer;
   }
   
   public String sayHello() {
      return "Hello " + name;
   }
   
   public void print() {
      this.printer.print(sayHello());
   }
}
