package Test1;

public class ZooMain {
    public static void main(String[] args) {
        Dog d = new Dog("旺财", 3);
        Animal a = new Cat("咪咪", 2);   // 父类引用指向子类对象
        d.makeSound();
        a.makeSound();
        // 猜猜会打印什么？
        d.fetch();
        //a.fetch();  // 父类引用不能调用子类特有的方法
        ((Cat)a).purr();  // 向下转型，调用子类特有的方法
    }
}
