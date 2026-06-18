package com.kopo.hkcode.annot;

import org.springframework.stereotype.Component;

@Component("consoleProinter")
public class ConsolePrinter implements Printer {

   @Override
   public void  print(String message) {
      System.out.println("Console  call" + message);

   }

}