package com.kopo;

public class Hello {

    public void publicMethod() {
        System.out.println("public access");
    }

    protected void protectedMethod() {
        System.out.println("protected access");
    }

    void defaultMethod() {
        System.out.println("default access");
    }

    private void privateMethod() {
        System.out.println("private access");
    }
}