package DataStructure;

/**
 * 🐳 默写题单：并查集（Union-Find）三档递进实现
 * 日期：2026-09-20 · 复习期第 2 周 · 数据结构线（补课日 15:30-16:30 段）
 *
 * 要求（一档写通再写下一档；三档用**同一组操作**在 main 里对拍，结果必须一致）：
 *  ① **QuickFind**：int[] groupId。find(x) 直接查数组 O(1)；union(x,y) 要遍历整个数组、
 *     把 y 那一组的 ID 全改成 x 的组 ID → O(n)
 *  ② **QuickUnion**：int[] parent（初始 parent[i] = i）。find 顺着父指针爬到根，O(h)；
 *     union 把一棵树的**根**挂到另一棵树的根下面
 *  ③ **优化版 QuickUnion**：find 时**路径压缩**（把路径上的节点直接连到根）＋ union 时
 *     **按大小 / 按秩合并**（小树接大树），使树高保持很低
 *
 * 自测期望（三档皆须满足）：
 *  初始 0..8 各自一组，依次执行：
 *      union(3,4) union(8,0) union(2,3) union(5,6) union(5,1) union(7,3) union(1,6) union(4,8)
 *  ① 执行完后 0..8 全部同根 → connected(0, 8)、connected(1, 4) ... 全 true
 *  ② 集合数量 count == 1（维护并打印）
 *  ③ 再 union(0,2) → count 仍为 1（已在同一集合，不做无谓合并）
 *  ④ 边界：n = 0 不崩；n = 1 时 find(0) == 0
 *  ⑤ 打印三档各自的最终 parent 数组，肉眼对比结构差异（QuickUnion 会成链，优化版是扁平/矮树）
 *
 * ⚠️ 核心考点（拷打会问）：
 *  - 三档复杂度：QuickFind find O(1) / union O(n) · QuickUnion find O(h)，最坏 O(n) · 优化后近似 O(α(n))
 *  - 最坏情况长什么样？（顺序 union(0,1) → union(1,2) → … 退化成一条链）
 *  - 路径压缩为什么能压高度？按大小合并为什么能防链？（两个优化解决的**不是同一个问题**）
 *  - 应用：连通性判断 · 朋友圈数量 · 岛屿数量 · **Kruskal 最小生成树**（W3 要用）
 */

// TODO 2026-09-20：主人默写区 —— 从空类开始写，三档按顺序来
public class UnionFind {
}
