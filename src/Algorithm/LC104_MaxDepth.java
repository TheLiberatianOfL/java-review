package Algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 🐳 LeetCode 104 · 二叉树的最大深度（热题100）
 * 日期：2026-09-19 · 复习期第 2 周 · 救火日
 * 力扣 AC：**递归版 40/40 · 0 ms 击败 100.00%**（提交 15:33）
 *          **BFS 版 40/40 · 2 ms 击败 11.14%**（提交 16:11）
 * 提交者：TheLiberationOfl
 *
 * 注：两版均为力扣 AC 原版（逻辑与变量名未改）；本文件补的是注释块 + 本地 main 自测，
 *     便于脱离力扣复跑。
 */
public class LC104_MaxDepth {

    public static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /* ================= ① 常规版：递归 DFS（力扣 0ms / 击败 100%）================= */

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /* ================= ② 另一解：BFS 层序遍历（迭代 · 防深链爆栈）=================
     *
     * 【为什么要有这一版 —— 不是"更快"，是"更稳"】
     *   递归版时间常数最小，但空间 = **O(树高 h)**：递归栈深度就是树高。
     *   树退化成链时 h = n → 本机实测：链长 1 万层尚可通过，**5 万层即 StackOverflowError**
     *   （临界点取决于 JVM 栈大小）。
     *   对照：同样 5 万个节点，**平衡树的递归深度只需 16 层** —— 节点数一样，栈深差 3000 倍。
     *
     * 【BFS 版为什么不怕深链】
     *   迭代 + 队列，**没有递归栈** → 空间 = O(最宽一层的节点数)，**与树高无关**。
     *   代价：每个入队节点都要分配队列节点对象，常数比递归大（力扣实测 2ms vs 0ms）。
     *
     * 【复杂度对比】
     *   时间：两者都是 O(n)（每个节点访问一次）—— **退化不影响时间**
     *   空间：DFS O(h)（h = 树高）｜ BFS O(width)（最宽一层节点数）
     *
     * 【本版最关键的技巧】
     *   `while` 转一圈 = 走完一整层；`size` 是「当层 / 下一层」的**分隔符**
     *   —— 队列里会同时混着当层节点和它们刚入队的孩子，不先记 size 就分不清
     *   这一层到哪儿结束，深度就会数不准。`height++` 写在"当层处理完"之后。
     *
     * 【与已写代码的联系】同一招在 `src/DataStructure/TreeTraversalIterative.java`
     *   的「层序遍历 + 按层分行」里已经用过（每层先记 size）—— 9/16 写过，今天才把它
     *   和"数层数"连起来。
     *
     * 【后续可优化】把 LinkedList 换成 ArrayDeque（数组实现、缓存友好），常数更小。
     */

    public int maxDepthBFS(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> n = new LinkedList<>();
        n.offer(root);
        int height = 0;
        while (!n.isEmpty()) {
            int size = n.size();
            for (int i = 1; i <= size; i++) {
                TreeNode a = n.poll();
                if (a.left != null) {
                    n.offer(a.left);
                }
                if (a.right != null) {
                    n.offer(a.right);
                }
            }
            height++;
        }
        return height;
    }

    /* ================= ③ main 自测（7 组 + 深链对照）================= */

    public static void main(String[] args) {
        System.out.println("== 递归版 / BFS 版逐项对比（题单判据）==");
        check("主用例 {3,9,20,-1,-1,15,7}          ", 3, new int[]{3, 9, 20, -1, -1, 15, 7});
        check("坑用例 空树                        ", 0, new int[]{});
        check("边界 {1}                          ", 1, new int[]{1});
        check("边界 {1,2}（只有左子）              ", 2, new int[]{1, 2});
        check("边界 {1,-1,2}（只有右子）           ", 2, new int[]{1, -1, 2});
        check("边界 {1,2,-1,3}（左斜链）           ", 3, new int[]{1, 2, -1, 3});
        check("边界 {1,2,-1,3,-1,-1,-1,4}（更长的链）", 4, new int[]{1, 2, -1, 3, -1, -1, -1, 4});

        System.out.println();
        System.out.println("== 深链对照：递归会爆栈、BFS 扛得住（本机实测）==");
        LC104_MaxDepth s = new LC104_MaxDepth();
        for (int len : new int[]{10000, 50000}) {
            TreeNode chain = buildChain(len);
            String r1, r2;
            try {
                r1 = "深度 " + s.maxDepth(chain);
            } catch (Throwable t) {
                r1 = "!! " + t.getClass().getSimpleName();
            }
            try {
                r2 = "深度 " + s.maxDepthBFS(chain);
            } catch (Throwable t) {
                r2 = "!! " + t.getClass().getSimpleName();
            }
            System.out.println("  链长 " + len + " ｜ 递归 -> " + r1 + " ｜ BFS -> " + r2);
        }
    }

    static void check(String name, int expect, int[] level) {
        LC104_MaxDepth s = new LC104_MaxDepth();
        TreeNode root = build(level);
        int a = s.maxDepth(root);
        int b = s.maxDepthBFS(root);
        boolean ok = (a == expect) && (b == expect);
        System.out.println((ok ? "  ✅ " : "  ❌ ") + name
                + " ｜ 递归 " + a + " ｜ BFS " + b + " ｜ 期望 " + expect);
    }

    /** 层序数组建树，-1 表示空位 */
    static TreeNode build(int[] level) {
        if (level.length == 0 || level[0] == -1) {
            return null;
        }
        TreeNode[] nodes = new TreeNode[level.length];
        for (int i = 0; i < level.length; i++) {
            nodes[i] = (level[i] == -1) ? null : new TreeNode(level[i]);
        }
        for (int i = 0; i < level.length; i++) {
            if (nodes[i] == null) {
                continue;
            }
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            if (l < level.length) {
                nodes[i].left = nodes[l];
            }
            if (r < level.length) {
                nodes[i].right = nodes[r];
            }
        }
        return nodes[0];
    }

    /** 建一条 len 层的左斜链 */
    static TreeNode buildChain(int len) {
        TreeNode root = null;
        for (int i = len; i >= 1; i--) {
            TreeNode node = new TreeNode(i);
            node.left = root;
            root = node;
        }
        return root;
    }
}
