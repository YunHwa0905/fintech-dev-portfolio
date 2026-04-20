package quiz;


interface Electric {
    void charge();
}

// 추상 클래스
abstract class Car {
    private String model;  // 접근제한자: private

    public Car(String model) {
        this.model = model;
    }

    // 공통 메서드
    public void introduce() {
        System.out.println("이 자동차는 " + model + "입니다");
    }

    // 추상 메서드
    abstract void drive();
}

// Sedan 클래스
class Sedan extends Car {

    public Sedan(String model) {
        super(model);
    }

    @Override
    void drive() {
        System.out.println("부드럽게 달린다");
    }
}

// Truck 클래스
class Truck extends Car {

    public Truck(String model) {
        super(model);
    }

    @Override
    public void drive() {
        System.out.println("짐을 싣고 달린다");
    }
}

// ElectricCar 클래스 (확장)
class ElectricCar extends Car implements Electric {

    public ElectricCar(String model) {
        super(model);
    }

    @Override
    void drive() {
        System.out.println("전기로 달린다");
    }

    @Override
    public void charge() {
        System.out.println("배터리를 충전한다");
    }
}

// 실행 클래스
public class car1 {
    public static void main(String[] args) {

        // 다형성: 부모 타입 배열
        Car[] cars = new Car[3];
        cars[0] = new Sedan("소나타");
        cars[1] = new Truck("포터");
        cars[2] = new ElectricCar("테슬라");

        for (Car c : cars) {
            c.drive();        // 오버라이딩 (동적 바인딩)
            c.introduce();    // 공통 메서드

            // 인터페이스 확인 후 실행
            if (c instanceof Electric) {
                ((Electric) c).charge();
            }

            System.out.println("------------------");
        }
    }
}