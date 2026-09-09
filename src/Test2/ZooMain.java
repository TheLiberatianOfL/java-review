package Test2;

public class ZooMain {
    public static void main(String[] args) {
        Animal d=new Dog("旺财");
        Feeder f=new Feeder();
        f.feed(d);
        Cat cat=new Cat("发财");
        f.feed(cat);
        //接口swwim不可创造实例，抽象类如此
        Swim sw = (Swim) d;                  // 向下转型:Animal 引用转成接口引用(昨天 ((Cat)a).purr() 同款)
        sw.swim();
    }
}
