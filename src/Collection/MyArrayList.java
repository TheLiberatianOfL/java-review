package Collection;

import java.util.Arrays;

/**
 * 🐳 默写题单：ArrayList 的扩容流程（手写一个简化版）
 * 日期：2026-09-14 · 复习期第 2 周 · Java 集合框架 A（W2 正课）
 * 讲义对应：`04-资料\Java\报班笔记\S3-01-集合与泛型-精要.md` §7 List 接口
 *
 * ⚠️ 本题的定位：**不是为了造轮子，是为了讲清 JDK 的 ArrayList 内部发生了什么**。
 *    面试原话：「讲讲 ArrayList 的扩容」——答的就是你下面要写的这段。
 *
 * ─────────────── 参数（先记住这三个数，题里只用它们）───────────────
 *   DEFAULT_CAPACITY = 10   默认容量
 *   GROWTH           = 1.5   扩容倍数（JDK 实际写法：old + (old >> 1)）
 *   懒加载            无参构造时**不分配数组**，第一次 add 才真正建出长度 10 的数组
 *
 * ─────────────── 要求（凭记忆写，不看 JDK 源码）───────────────
 *  1. 字段：int[] elementData; int size;
 *  2. 构造器：
 *       MyArrayList()        → 懒加载（数组先不建）
 *       MyArrayList(int cap) → 直接建 cap 长度的数组（cap 不合法时怎么处理？自己定并注释）
 *  3. add(int value)
 *        - 若数组还没建（null）→ 先建默认容量的数组
 *        - 若 size == elementData.length → **扩容**（1.5 倍，写清怎么算）
 *        - 放进去，size++
 *  4. get(int index) / set(int index, int value)   ← 越界要抛异常（用什么异常？自己定）
 *  5. size()
 *  6. **扩容方法 private void grow()** —— 本题的主角，写清三点：
 *        ① 新容量怎么算（1.5 倍的具体写法）
 *        ② 元素怎么搬（用哪个方法搬？）
 *        ③ 老数组怎么办（会被回收吗？为什么）
 *
 * ─────────────── 自测期望（自己写 main 打印，别只看代码）───────────────
 *  ① 懒加载验证：
 *       MyArrayList a = new MyArrayList();
 *       size == 0；且**此刻内部数组还是 null**（写个 getCapacity() 暴露数组长度来验证）
 *  ② 首次 add：
 *       a.add(1) 之后 → getCapacity() == 10
 *  ③ 扩容触发点：
 *       连续 add 10 个后 → getCapacity() == 10（正好装满，还没扩）
 *       再 add 第 11 个  → getCapacity() == 15（10 → 15，1.5 倍）
 *  ④ 元素不丢：
 *       11 个元素全在，顺序不变（打印 toString 或逐个 get 验证）
 *  ⑤ 再扩容：
 *       加到第 16 个 → getCapacity() == 22（15 → 22，注意是 (int)(15 * 1.5)）
 *
 * ─────────────── 边界用例（必跑）───────────────
 *  - 容量为 0 的构造（MyArrayList(0)）：第一次 add 会怎样？（先建容量 1？还是用默认 10？自己定，注释写理由）
 *  - get(负数) / get(size) → 必须抛异常，不能返回垃圾值
 *  - 空列表 add 第一个元素
 *
 * ⚠️ 核心考点（面试常问，答不上就是白写）：
 *  - **扩容为什么是 1.5 倍而不是 2 倍？**（提示：空间与时间的折中；2 倍的是 HashMap，别搞混）
 *  - **无参构造为什么懒加载？**（存一个空表给每个对象都白占 10 个格子）
 *  - **扩容的代价是什么？**（`Arrays.copyOf` 是 O(n)，所以"add 是均摊 O(1)，但某一次可能很慢"）
 *  - **删元素会缩容吗？**（不会 —— 顺带回答"ArrayList 占内存不还"这个问题）
 */

// TODO 2026-09-14：主人默写区 —— 先写字段和方法骨架，再写 main 跑上面 5 组自测期望
public class MyArrayList {
    // 1) 字段：int[] elementData; int size;
    // 2) 两个构造器（无参 = 懒加载）
    // 3) add / get / set / size / grow / getCapacity
    // 4) main：跑 ①~⑤ 五组自测 + 边界用例
    final static int DEFAULT_CAPACITY = 10;
    final static double DEFAULT_LOAD_FACTOR = 1.5;
    private int[] elementData;

    private int size=0;
    public MyArrayList() {

    }
    public MyArrayList(int capacity){
        elementData = new int[capacity];
    }
    public int get(int index) {
        if (index >= size||index<0){
            throw new ArrayIndexOutOfBoundsException("下标越界：" + index + "，当前 size：" + size);
        }
        return elementData[index];
    }
    public int set(int index, int value) {
        if (index >= size||index<0){
            throw new ArrayIndexOutOfBoundsException("下标越界：" + index + "，当前 size：" + size);
        }
        int oldIndex = elementData[index];
        elementData[index] = value;
        return oldIndex;
    }

    public int getCapacity() {
        return elementData==null?0:elementData.length;
    }
    public void add(int element) {
        if(elementData==null)
            elementData = new int[DEFAULT_CAPACITY];
        if (size == elementData.length) {
           elementData= Arrays.copyOf(elementData, (int) (elementData.length*DEFAULT_LOAD_FACTOR));
        }
        elementData[size] = element;
        size += 1;
    }
    public int size(){
        return size;
    }

    public static void main(String[] args) {
        MyArrayList a = new MyArrayList();
        System.out.println("构造后容量 = " + a.getCapacity());        // 期望 0（数组还没建）

        a.add(1);
        System.out.println("首次 add 后容量 = " + a.getCapacity());   // 期望 10

        for (int i = 2; i <= 10; i++) a.add(i);
        System.out.println("装满 10 个后容量 = " + a.getCapacity());  // 期望 10（还没扩）

        a.add(11);
        System.out.println("第 11 个后容量 = " + a.getCapacity());    // 期望 15

        for (int i = 12; i <= 16; i++) a.add(i);
        System.out.println("第 16 个后容量 = " + a.getCapacity());    // 期望 22

        // 再补两行：验证元素没丢（11 个元素顺序不变，用 get 逐个打印）
        for(int i=0; i<a.size(); i++){
            System.out.println(a.get(i));
        }
    }
}
