package Collection;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * 🐳 默写题单：迭代器 与 for-each 的底层（W3 补课 · 2026-09-21）
 * 背景：W2「集合收口」里列过"迭代器要点"但一直没执行 → 今天补上（约 20 分钟）
 * 参考笔记：`04-资料\Java\03-高级API\集合框架\串讲笔记-集合与泛型(2026-09-20).md` §5
 *
 * 要求（四小问，写完在 main 里跑出来）：
 *  ① **for-each 遍历**：`for (String s : list)` 打印每个元素（你最常用的写法）
 *  ② **显式迭代器遍历**：
 *        Iterator<String> it = list.iterator();
 *        while (it.hasNext()) { String s = it.next(); ... }
 *     ⚠️ 想清楚：① 和 ② 本质上是同一个东西吗？为什么？
 *  ③ **复现 ConcurrentModificationException**：在 for-each 里直接 `list.remove(...)`
 *     → 用 try/catch 捕获，打印**异常类型**（这就是 CME）
 *  ④ **正确删除姿势**：改用 `Iterator.remove()`（或 `list.removeIf(s -> ...)`），删完打印结果
 *
 * 自测期望：
 *  ① for-each 输出：      a b c
 *  ② 显式迭代器输出：      a b c（与 ① 一致 —— 因为底层就是同一个）
 *  ③ 捕获到异常类型：      java.util.ConcurrentModificationException
 *  ④ 正确删除后：         [a, c]（删掉 b，且无异常）
 *
 * ⚠️ 拷打必问（写完要能张口答）：
 *  - **CME 的成因**是什么？（提示：`modCount` / `expectedModCount` / fail-fast 快速失败）
 *  - 为什么 `Iterator.remove()` 就**不炸**？
 *  - 在 for-each 里删完**立刻 break**，还会炸吗？为什么？（面试陷阱题）
 *  - 除了 for-each 删元素，还有哪些"看着安全其实会炸"的同类写法？
 */

// TODO 2026-09-21：主人默写区
public class IteratorPractice {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        for (String s : list){
            System.out.println(s);
        }
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        try {
            for (String s : list) {
                if (s.equals("A")) list.remove("A");   // 这一句本身不报错
            }                                          // ← 报错发生在这里：下一次 next()
        } catch (ConcurrentModificationException e) {
            System.out.println("抓到 CME -> " + e.getClass().getName());
        }
        Iterator<String> iterator1 = list.iterator();
        while (iterator1.hasNext()){
            String s = iterator1.next();
            if (s.equals("A")) iterator1.remove();
        }



    }
}
