package quiz;

public class carQuiz {

	// abstract class: 공통 속성과 메서드 정의, 직접 인스턴스화 불가
	abstract class Car {
		private String model; // 캡슐화: 외부 직접 접근 차단

		Car(String model) {
			this.model = model;
		}

		abstract void drive(); // 하위 클래스에서 반드시 구현 강제

		void info() {
			System.out.println("이 자동차는 " + model + "입니다.");
		}
	}

	class Sedan extends Car implements Electric {

		Sedan(String model) {
			super(model);
		}

		@Override
		void drive() {
			System.out.println("부드럽게");
		}

		@Override
		public void charge() {
			System.out.println("배터리를 충전한다.");
		}
	}

	class Truck extends Car {

		Truck(String model) {
			super(model);
		}

		@Override
		void drive() {
			System.out.println("짐을 싣고");
		}
	}

	// 인터페이스: 전기차 충전 기능이라는 규약 정의
	interface Electric {
		void charge();
	}

	// ElectricCar: Car 상속 + Electric 구현
	class ElectricCar extends Car implements Electric {
		ElectricCar(String model) {
			super(model);
		}

		@Override
		void drive() {
			System.out.println("부릉부릉");
		}

		@Override
		public void charge() {
			System.out.println("전기로 달린다.");
		}

	}

	public static void main(String[] args) {
		carQuiz system = new carQuiz();

		// 다형성: 부모 타입 Car 배열로 서로 다른 자식 객체를 통합 관리
		Car[] cars = new Car[3];
		cars[0] = system.new Sedan("소나타");
		cars[1] = system.new Truck("포터");
		cars[2] = system.new ElectricCar("전기차");

		for (Car c : cars) {
			c.info();
			c.drive();

			// instanceof: 런타임에 객체 타입 확인 후 안전하게 다운캐스팅
			if (c instanceof Electric) {
				((Electric) c).charge();
			}
			System.out.println();
		}
	}

}
