package quiz;

public class animalSystem {
	abstract class Animal {
		private String name;

		abstract void sound();

		void introduce(String n) {
			this.name = n;
			System.out.println("나는 " + this.name + "이다");
		};
	}

	class Dog extends Animal implements Moveable{

		@Override
		void sound() {
			System.out.println("멍멍");
		}
		
		public void move() {
			System.out.println("뛰어다닌다");
		}
	}

	class Cat extends Animal implements Moveable{

		@Override
		void sound() {
			System.out.println("야옹");
		}
		
		public void move() {
			System.out.println("살금살금 걷는다");
		}
	}
	
	interface Moveable{
		void move();
	}
	

	public static void main(String[] args) {
		
		animalSystem system = new animalSystem();

        Animal[] animals = new Animal[2];
        animals[0] = system.new Dog("바둑이");
        animals[1] = system.new Cat("나비");
        
        for(Animal a : animals) {
        	a.sound();
        	a.introduce();
        }
        
		
	}

}
