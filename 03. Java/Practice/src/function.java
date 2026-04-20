class Greeter{
	void sayHello() {
		System.out.println("Hello");
	}
	
	void sayHello(String str) {
		System.out.println("Hello" + str);
	}
	
	String sayHello3(String str1, String str2) {
		String strTemp = "Hello " + str1 + " " + str2;
		System.out.println(strTemp);
		return "Completed";
	}
}


public class function {

	public static void main(String[] args) {
		Greeter g = new Greeter();
		g.sayHello();
		g.sayHello("JAVA");
		g.sayHello3("A", "B");
		System.out.println(g.sayHello3("C", "D"));
	}
}
