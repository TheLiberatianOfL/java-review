package Test1;

public class Cat extends Animal{
    public Cat(String name,int age){
        super(name,age);
    }
    @Override
    public void makeSound(){
        System.out.println(getName()+" meows");

    }
    public void purr(){
        System.out.println(getName()+" is purring");
    }
}
