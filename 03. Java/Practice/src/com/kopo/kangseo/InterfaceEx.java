package com.kopo.kangseo;

interface Payment {
    void pay(int amt);
}

class Card implements Payment {
    @Override
    public void pay(int amt) {
        System.out.println("Card Pay: " + amt);
    }
}

class Cash implements Payment {
    @Override
    public void pay(int amt) {
        System.out.println("Cash Pay: " + amt);
    }
}

class NaverPay implements Payment {
    @Override
    public void pay(int amt) {
        System.out.println("Naver Pay: " + amt);
    }
}

class Service {
    private final Payment p;

    public Service(Payment p) {
        this.p = p;
    }

    void run(int amt) {
        p.pay(amt);
    }
}

//////////////////////////////////////////////////
// 🔥 퍼사드 클래스 추가
//////////////////////////////////////////////////
class PaymentFacade {

    public void payByCard(int amt) {
        Service s = new Service(new Card());
        s.run(amt);
    }

    public void payByCash(int amt) {
        Service s = new Service(new Cash());
        s.run(amt);
    }

    public void payByNaver(int amt) {
        Service s = new Service(new NaverPay());
        s.run(amt);
    }
}

//////////////////////////////////////////////////
// 🔥 사용 코드 (훨씬 단순해짐)
//////////////////////////////////////////////////
public class InterfaceEx {
    public static void main(String[] args) {

        PaymentFacade facade = new PaymentFacade();

        facade.payByCard(100);
        facade.payByCash(200);
        facade.payByNaver(300);
    }
}