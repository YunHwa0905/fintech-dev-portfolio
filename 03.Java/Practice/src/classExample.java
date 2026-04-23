class Point {
    int x;
    int y;
}

public class classExample {
	 public static void main(String[] args) {

	        // 인스턴스 1
	        Point p1 = new Point();
	        p1.x = 10;
	        p1.y = 20;

	        // 인스턴스 2
	        Point p2 = new Point();
	        p2.x = 30;
	        p2.y = 40;

	        // 인스턴스 3
	        Point p3 = new Point();
	        p3.x = -5;
	        p3.y = 15;

	        // 값 출력
	        System.out.println("p1: (" + p1.x + ", " + p1.y + ")");
	        System.out.println("p2: (" + p2.x + ", " + p2.y + ")");
	        System.out.println("p3: (" + p3.x + ", " + p3.y + ")");
	        
	        // 배열에 담기
	        Point[] points = { p1, p2, p3 };

	        // 배열 순회하면서 출력
	        for (int i = 0; i < points.length; i++) {
	            System.out.println(
	                "points[" + i + "]: (" + points[i].x + ", " + points[i].y + ")"
	            );
	        }
	    }
}