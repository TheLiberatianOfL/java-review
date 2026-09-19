package Collection;

/**
 * 🐳 默写题单：HashMap 的 **put 流程**（手写一个简化版哈希表）
 * 日期：2026-09-14 · 复习期第 2 周 · Java 集合框架 B（W2 正课）
 * 讲义对应：`04-资料\Java\报班笔记\S3-01-集合与泛型-精要.md` §10 Map 接口
 *
 * ⚠️ 定位：**不是为了造轮子，是为了能默画出 JDK HashMap 的 put 流程**。
 *    精要原话：「面试权重 ★★★ —— **HashMap 是必问 top3**」，这题是复习期硬默写项。
 *    本题**不写红黑树**（JDK 的链表 > 8 才转红黑树）—— 你只写"数组 + 链表"就够了，
 *    但**能讲清"为什么要转红黑树"**。
 *
 * ─────────────── 参数（先记住，题里只用它们）───────────────
 *   DEFAULT_CAPACITY = 16     默认容量（注意！ArrayList 是 10，HashMap 是 16）
 *   LOAD_FACTOR      = 0.75   负载因子
 *   扩容规则          = 容量 × 2（**2 倍**！ArrayList 才是 1.5 倍，别搞混）
 *   扩容时机          = size > capacity × 0.75
 *   树化阈值          = 单个链表长度 > 8 时，JDK 转红黑树（本题只要求"讲得出"，不用实现）
 *
 * ─────────────── 要求（凭记忆写）───────────────
 *  1. 节点类：static class Node { int key; int value; Node next; }（值先用 int，别用泛型）
 *  2. 字段：Node[] table; int size;
 *  3. **static int hash(int key)**：先只做 `key` 本身（简单版）；写完注释一句：
 *       「JDK 还要做一次扰动（高 16 位异或低 16 位），目的是什么？」
 *  4. **static int indexFor(int hash, int capacity)**：`hash % capacity`
 *       写完后注释回答：「为什么 JDK 用 `(n-1) & hash` 这个位运算？它成立的前提是什么？」
 *  5. **put(int key, int value)** —— 本题主角，按顺序做四步：
 *       ① 算下标（用上面的 hash + indexFor）
 *       ② 该位置为空 → 直接放新节点
 *       ③ 不为空 → 沿链表找：**找到相同 key 就覆盖 value**，没找到就**挂在链表末尾**（或头插，自己选并注释理由）
 *       ④ **插完检查要不要扩容**（size > capacity × 0.75）→ 扩容是**2 倍**，且元素要**重新分配下标**
 *  6. get(int key)：算下标 → 沿链表找 key → 返回 value；找不到返回什么？（自己定并注释）
 *  7. size() / getCapacity()（暴露 table.length，供自测用）
 *  8. **private void resize()**：容量翻倍 + 把旧数据搬过去
 *       注释回答：「扩容后元素位置会变吗？为什么 JDK 里**不用重新算 hash**？」
 *
 * ─────────────── 自测期望（自己写 main 打印）───────────────
 *  ① 初始：new 出来后 table 是 null（懒加载）→ 第一次 put 才建出容量 16 的数组
 *  ② 单点存取：put(1, "A") → get(1) 得 "A"
 *  ③ **覆盖语义**：put(1, "A") 后再 put(1, "B") → get(1) 得 "B"，且 size **仍然是 1**
 *  ④ **冲突（必测）**：找两个**下标相同**的 key（如 1 与 17，容量 16 时 `1 % 16 == 17 % 16`）
 *       → 两个都 put 进去，size == 2；两个都能 get 到正确 value（说明链表接对了）
 *  ⑤ **扩容触发**：容量 16、负载因子 0.75 → 第 **13** 个 put 时触发扩容（16 → 32）
 *       验证：put 到第 12 个时 getCapacity() == 16；第 13 个之后 == 32
 *  ⑥ **扩容后能查到**：扩容前的所有 key 在扩容后依然 get 得到（说明搬对了）
 *  ⑦ 不存在 key：get(999) 返回你定的那个值
 *
 * ─────────────── 边界用例（必跑）───────────────
 *  - put 到空表（table == null）第一个元素
 *  - 同一个 key 反复 put 三次（size 不应该涨）
 *  - 负数 key（`-1 % 16` 是什么？**注意 Java 的负数取模仍为负** —— 这是本题最大的坑，
 *      想一想：下标算出负数会怎样？JDK 怎么规避的？）
 *
 * ⚠️ 核心考点（面试原话就在这些点上）：
 *  - **put 的完整四步**（算下标 → 空位直放 → 冲突则链表查找/覆盖/追加 → 检查扩容）
 *  - **默认容量 16、扩容 2 倍、负载因子 0.75** —— 三个数字必须张口就来
 *  - **为什么容量是 2 的幂？**（因为 `(n-1) & hash` 等价于取模，但更快）
 *  - **链表为什么转红黑树？**（链表查找 O(n) → 红黑树 O(log n)，防哈希攻击）
 *  - **HashMap 线程安全吗？**（❌ 并发 put 会丢数据 / size 不准；并发用 `ConcurrentHashMap`）
 *  - `HashMap` vs `Hashtable`：允许 null 吗？（HashMap 允许 1 个 null key、多个 null value）
 */

// TODO 2026-09-14：主人默写区 —— 先写 Node + 三个方法（hash/indexFor/put），再补 get 与 resize

public class MyHashMap {
    // 1) static class Node { int key; int value; Node next; }
    // 2) Node[] table; int size;
    // 3) hash / indexFor / put / get / resize / size / getCapacity
    // 4) main：跑 ①~⑦ 自测 + 边界用例（尤其负数 key）
}
