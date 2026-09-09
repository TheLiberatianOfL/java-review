package Test2;

public abstract class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;    // 构造器接住参数,赋给 private 字段(封装:赋值走构造器/setter)
    }

    abstract void eat();
    abstract void makeSound();
    
}
