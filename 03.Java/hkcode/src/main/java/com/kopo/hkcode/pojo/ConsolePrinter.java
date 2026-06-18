package com.kopo.hkcode.pojo;

public class ConsolePrinter implements Printer {

   @Override
   public void  print(String message) {
      System.out.println("Console  call" + message);

   }

}