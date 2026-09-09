package Test2;

public class Dog extends Animal implements Swim {

    public Dog(String name) {
        super(name);

    }
    @Override
    void eat() {
        System.out.println("Dog eats bones" );
    }

    @Override
    void makeSound() {
        System.out.println("Dog bark");
    }

    @Override
    public void swim() {
        System.out.println("Dog swims");
    }
}
