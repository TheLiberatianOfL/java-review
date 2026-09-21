package Collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 🐳 默写题单：泛型（W3 复习 · 2026-09-21）
 * 参考笔记：`04-资料\Java\03-高级API\集合框架\串讲笔记-集合与泛型(2026-09-20).md` §6
 * 复习期定位：泛型主人已学过 → **脱稿写**（卡住再问，不许直接抄笔记）
 *
 * 要求：
 *  ① **泛型方法**：`public static <T extends Comparable<T>> T max(List<T> list)` —— 返回集合里最大的元素
 *     ⚠️ 想清楚：`<T extends Comparable<T>>` 这个上界为什么必须加？（不加会怎样）
 *  ② **泛型类**：`Pair<K, V>` —— 两个字段 + 全参构造 + getter + `toString()`（格式 `key=value`）
 *  ③ **通配符**：`public static double sum(List<? extends Number> list)` —— 求和（用 `doubleValue()`）
 *     ⚠️ 想清楚：这里为什么用 `extends` 而不是 `super`？
 *
 * 自测期望（main 里跑出来并打印）：
 *  ① max(Arrays.asList(3, 7, 2, 9, 5))                    → 9
 *  ② max(Arrays.asList("banana", "apple", "cherry"))      → cherry（String 自带 Comparable）
 *  ③ new Pair<>("age", 19).toString()                     → age=19
 *  ④ sum(Arrays.asList(1, 2, 3))                          → 6.0
 *  ⑤ sum(Arrays.asList(1.5, 2.5))                         → 4.0（同一个方法吃两种元素类型 = 泛型的意义）
 *
 * ⚠️ 拷打必问（写完要能张口答）：
 *  - **类型擦除**是什么？`List<String>` 和 `List<Integer>` 在运行期是同一个类吗？怎么验证？
 *  - `List<?>` 能 `add` 吗？`List<Object>` 能吗？为什么？
 *  - **PECS** 全称是什么？`extends` 和 `super` 各用在哪一侧？
 *  - `List<int>` 能编译吗？为什么不行？
 */

// TODO 2026-09-21：主人默写区 —— 从空类开始写
public class GenericPractice {
    public static <T extends Comparable<T>> T max(List<T> list) {
        T max = list.get(0);
        int i=1;
        while(i<list.size()) {
            if(list.get(i).compareTo(max) > 0) {
                max = list.get(i);

            }
            i++;
        }
        return max;
    }
    public static class Pair<K,V>{
        private K key;
        private V value;
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        @Override
        public String toString() {
            return key + "=" + value;
        }


    }
    public static double sum(List<? extends Number> list){
        double sum = 0;
        for(Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }
    public static void main(String[] args) {
        int a=max(Arrays.asList(3, 7, 2, 9, 5));
        System.out.println(a);
        String b=max(Arrays.asList("banana", "apple", "cherry"));
        System.out.println(b);
        Pair<String,Integer> c=new Pair<>("age", 19);
        System.out.println(c.toString());
        System.out.println(sum(Arrays.asList(1, 2, 3)));;
        System.out.println(sum(Arrays.asList(1.5, 2.5)));

    }



}
