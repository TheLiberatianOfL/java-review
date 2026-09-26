package DataStructure;

import java.util.*;

/**
 * GraphTraversal —— 图的遍历：DFS（递归 / 非递归）+ BFS
 *
 * 讲义对应：数据结构直播课 · 3.图的概述 + 图的遍历
 *          （笔记：04-资料\数据结构\报班笔记\3-图的概述-精要.md §⑦）
 * 日期：2026-09-26 · 复习期 W3 · 判据「能讲清 DFS 与 BFS 的差异与适用场景」
 *
 * ────────────────────────────────────────────────
 * 自测期望（示例图见 main）
 *   顶点 0..4，无向边 (0,1)(0,2)(1,3)(2,3)(3,4)，邻接表按"加入顺序尾插"
 *   期望邻接表：0:[1,2]  1:[0,3]  2:[0,3]  3:[1,2,4]  4:[3]
 *
 *   ① dfsRecursive(0)  → 0 1 3 2 4
 *   ② bfs(0)           → 0 1 2 3 4
 *   ③ dfsIterative(0)  → 用「pop 即访问 + 把未访问邻居依次压栈」这个常见写法，
 *                        输出 0 2 3 4 1
 *                        ✅ 实测即为 0 2 3 4 1（与递归版不同，因邻居入栈顺序与出栈顺序相反）
 *   ④ 非连通图（加孤立顶点 5，共 6 个顶点）：遍历要能覆盖全部 6 个 →
 *      需要对「每个未访问顶点各起一次」（这就是"连通分量"的用武之地）✅ 实测 0 1 3 2 4 5
 *   ⑤ 边界：单顶点无边 → 输出它自己；空图（0 顶点）→ 输出空，不抛异常
 *
 * ────────────────────────────────────────────────
 * 可用工具（Java 里没有手写指针链表，用现成的；下面是最小用法，不是要你原样抄）
 *   List<List<Integer>> adj = new ArrayList<>();
 *   adj.add(new ArrayList<>());        // 加一个顶点（它的邻接表初始为空）
 *   adj.get(0).add(1);                 // 往 0 的邻接表里加邻居 1
 *   adj.get(0).size()                  // 0 有几个邻居
 *   adj.get(0).get(i)                  // 0 的第 i 个邻居（下标从 0 起）
 *
 *   ArrayDeque<Integer> stack = new ArrayDeque<>();   // 当栈用（后进先出）
 *   stack.push(1);   stack.pop();   stack.isEmpty();  // 注意：pop 出来的是最后压进去的
 *
 *   ArrayDeque<Integer> queue = new ArrayDeque<>();   // 当队列用（先进先出）
 *   queue.offer(1);  queue.poll();  queue.isEmpty();  // offer 进队尾，poll 出队头
 *
 *   boolean[] visited = new boolean[n];               // 默认全是 false，直接当标记用
 */
public class GraphTraversal {

    // 建图：无向图的一条边要写两份（0 的邻居里有 1，1 的邻居里也要有 0）
    static List<List<Integer>> buildGraph(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();   // 一个空的「盒子列表」
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());                // 给每个顶点发一个空盒子
        }
        for (int[] e : edges) {                        // 遍历每条边
            int a = e[0];
            int b = e[1];
            adj.get(a).add(b);                         // a 的盒子里放 b
            adj.get(b).add(a);                         // b 的盒子里放 a ← 无向图要写两份
        }
        return adj;
    }

    // DFS 递归版 —— 访问当前顶点，然后对所有未访问邻居递归
    // 出口：树靠「判空」返回，图靠「visited 把关」——循环走完这一层自然返回
    static List<Integer> dfsRecursive(List<List<Integer>> adj, int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[adj.size()];   // 新建的 boolean 数组默认全是 false，不用手动填
        dfs(adj, start, visited, result);
        return result;
    }
    static void dfs(List<List<Integer>> adj, int cur, boolean[] visited, List<Integer> result) {
        visited[cur] = true;          // 先标记再深入：这个点是「已经走过」的
        result.add(cur);
        for (int next : adj.get(cur)) {
            if (!visited[next]) {     // 图的 DFS 出口就在这道闸门：只放没走过的邻居进去
                dfs(adj, next, visited, result);
            }
        }
    }

    // DFS 非递归版 —— 用 ArrayDeque 当栈，自己模拟递归的进栈/出栈
    // 与递归版的顺序差异：递归「取第一个未访问邻居立刻深入」，本版「把邻居全压栈」→ 栈顶是最后一个 → 先走右支
    static List<Integer> dfsIterative(List<List<Integer>> adj, int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[adj.size()];
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        visited[start] = true;
        stack.push(start);

        while (!stack.isEmpty()) {
            int cur = stack.pop();                        // 栈顶取出（与 push 同在头部，后进先出）

            result.add(cur);
            for (int next : adj.get(cur)) {
                if (!visited[next]) {
                    visited[next] = true;
                    stack.push(next);                     // 压到栈顶
                }
            }
        }
        return result;
    }

    // BFS —— 用 ArrayDeque 当队列，一层一层往外扩
    // 标记必须放在「入队前」：visited 防的是「重复入队」，不是「重复访问」（否则同一个顶点会被塞进队列两次）
    static List<Integer> bfs(List<List<Integer>> adj, int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[adj.size()];
        ArrayDeque<Integer> queue = new ArrayDeque<>();   // 队列：先进先出

        visited[start] = true;
        queue.offer(start);                               // offer = 送进队尾

        while (!queue.isEmpty()) {
            int cur = queue.poll();                       // 队头取出（offer 进队尾 / poll 出队头 → 先进先出）
            result.add(cur);

            for (int next : adj.get(cur)) {
                if (!visited[next]) {
                    visited[next] = true;                 // 必须在入队前标记，否则同一顶点会重复入队
                    queue.add(next);                      // 送进队尾（等价 offer）
                }
            }
        }
        return result;
    }

    // 整图遍历（非连通图）—— 对每个未访问的顶点各起一次 DFS
    // visited 必须写在方法内、跨多次 dfs 复用：每起一次 DFS 就 new 一份 → 已走过的顶点被当新起点 → result 里出现重复顶点
    // 「起几次 DFS」= 这张图有几个连通分量
    static List<Integer> dfsAll(List<List<Integer>> adj) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[adj.size()];   // 一份 visited 跨多次 DFS 复用

        for (int i = 0; i < adj.size(); i++) {
            if (!visited[i]) {                         // ← 你答的那个条件
                dfs(adj, i, visited, result);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // 自测 1：连通图（5 顶点 5 边）
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {2, 3}, {3, 4}};
        List<List<Integer>> g = buildGraph(5, edges);
        System.out.println("邻接表  = " + g + "   期望 [[1, 2], [0, 3], [0, 3], [1, 2, 4], [3]]");
        System.out.println("DFS递归 = " + dfsRecursive(g, 0) + "   期望 [0, 1, 3, 2, 4]");
        System.out.println("DFS非递归 = " + dfsIterative(g, 0) + "   常见期望 [0, 2, 3, 4, 1]");
        System.out.println("BFS     = " + bfs(g, 0) + "   期望 [0, 1, 2, 3, 4]");

        // 自测 2：非连通图（顶点 5 是孤立点）
        List<List<Integer>> g2 = buildGraph(6, edges);
        System.out.println("全图DFS = " + dfsAll(g2) + "   期望覆盖 0..5 共 6 个顶点");

        // 自测 3：边界 —— 单顶点无边
        System.out.println("单点DFS = " + dfsRecursive(buildGraph(1, new int[][]{}), 0) + "   期望 [0]");
        System.out.println("单点BFS = " + bfs(buildGraph(1, new int[][]{}), 0) + "   期望 [0]");

        // 自测 4：边界 —— 空图（0 顶点）
        System.out.println("空图全遍历 = " + dfsAll(buildGraph(0, new int[][]{})) + "   期望 []");
    }
}
