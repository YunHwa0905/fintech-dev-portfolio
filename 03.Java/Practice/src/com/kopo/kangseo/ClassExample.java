package com.kopo.kangseo;


class MyBeeper {
	private int myBeeper;
	public int getMyBeeper() {
		return myBeeper;
	}
	public void setMyBeeper(int myBeeper) {
		this.myBeeper = myBeeper;
	}
	protected void call() {
		System.out.println("Beep...");
	}
}


class MyPhone extends MyBeeper {
	@Override
	protected void call() {
		super.call();
		System.out.println("Calling...");
		this.phonebook();
	}
	
	void phonebook() {
		System.out.println("PhoneBook...");

	}
}

class MySmartPhone extends MyPhone {
	void browseWeb() {
		System.out.println("Surfing...");
	}
}


public class ClassExample {

	public static void main(String[] args) {
		MySmartPhone myPhone = new MySmartPhone();
		// Inherited from Phone
		myPhone.call();
		// Defined in SmartPhone
		myPhone.browseWeb();
		
		System.out.println(myPhone instanceof MySmartPhone);
		System.out.println(myPhone instanceof MyPhone);
		System.out.println(myPhone instanceof MyBeeper);
	}

}