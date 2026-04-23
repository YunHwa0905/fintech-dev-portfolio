package com.kopo.kangseo;

class Beeper {
	Beeper(){
		System.out.println("Beeper 생성자 호출되었음");
	}
	void beep() {
		System.out.println("Beep...");
	}
}

class Phone extends Beeper {
	Phone(){
		System.out.println("Phone 생성자 호출되었음");
	}
	public String number = "02-123-4567";
	void call() {
		System.out.println("Calling...");
	}
}

class SmartPhone extends Phone {
	SmartPhone(){
		System.out.println("SmartPhone 기본 생성자 호출되었음");
	}
	SmartPhone(String str){
		System.out.println("SmartPhone 생성자 호출되었음 "+str);
	}
	private String number ="010-1234-1234";
	void browseWeb() {
		System.out.println("Surfing..."+this.number);
		System.out.println("Surfing..."+super.number);
	}
}


public class inheritanceTest {

	public static void main(String[] args) {
		SmartPhone sp = new SmartPhone();
		SmartPhone spIphone = new SmartPhone("iPhone");
		
		sp.call();
		sp.browseWeb();
		sp.beep();
		
		spIphone.call();
		spIphone.browseWeb();
		spIphone.beep();
	}

}