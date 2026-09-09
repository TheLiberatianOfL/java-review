package Test2;

public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }
    @Override
    void eat() {
        System.out.println("cat eat fish");
    }

    @Override
    void makeSound() {
        System.out.println("cat mewo");

    }
}
