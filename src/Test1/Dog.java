package Test1;

public class Dog extends Animal
{
    public Dog(String name, int age) {
        super(name, age);
    }
    @Override
    public void makeSound(){
        System.out.println("Dog barks");
    }
    public void fetch(){
        System.out.println(getName()+" is fetching the ball");
    }
}
