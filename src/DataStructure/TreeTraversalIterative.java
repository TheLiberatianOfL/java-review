package DataStructure;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 🐳 默写题单：二叉树的非递归遍历（先/中/后 · 用栈）+ 层序遍历（用队列）
 * 日期：2026-09-16 · 复习期第 2 周 · W2 树模块第 2 步（承接 BinaryTree 建树 + 递归遍历）
 *
 * 讲义对应：`04-资料\数据结构\报班笔记\2-树、二叉树、线索二叉树-精要.md` §7 遍历（★★重点）
 *
 * ═══════════ 核心那句话（本关的全部钥匙）═══════════
 *   **栈 = 后进先出** → 刚发现的孩子立刻被拿出来 → 一路往深处钻 → 深度优先（DFS）
 *   **队列 = 先进先出** → 最早发现的先处理 → 一层铺完再铺下一层 → 广度优先（BFS）
 *   所以：递归版里那份"系统调用栈"，非递归版要**自己拿一个栈复刻**；
 *        而层序只是**把栈换成队列**。
 * ══════════════════════════════════════════════════
 *
 * ── 2026-09-16 陪练版：主人当日确认「集合 API 不会 + 思路不清 + 要手把手」，以下由鲸鱼娘逐行讲解后落盘 ──
 */

public class TreeTraversalIterative {

    /**
     * ① 层序（BFS）：队列
     *    根入队 → 循环 { 出队打印 → 左孩子入队 → 右孩子入队 }
     */
    static void levelOrder(BinaryTree.TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<BinaryTree.TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            BinaryTree.TreeNode cur = queue.poll();   // 出队 → 处理
            System.out.print(cur.data + " ");

            if (cur.left != null) {                   // 左孩子入队（先左后右 = 层序要求的从左到右）
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }
    }

    /**
     * ② 层序 · 按层分行（P1）
     *    关键：**进循环前先记下"当前队列里有几个"** —— 那正好是这一层的节点数
     */
    static void levelOrderByLine(BinaryTree.TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<BinaryTree.TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();             // ⭐ 这一层有几个（必须在 for 之前取，因为队列会变）

            for (int i = 0; i < levelSize; i++) {     // 只出队这么多 —— 多出来的就是下一层了
                BinaryTree.TreeNode cur = queue.poll();
                System.out.print(cur.data + " ");

                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
            System.out.println();                     // 一层打完，换行
        }
    }

    /**
     * ③ 前序 · 非递归：根 → 左 → 右
     *    根入栈 → 循环 { 弹一个就打印 → **先压右、再压左** }
     *    为什么是"右先压"？因为栈是后进先出——后压的左孩子会**先**被弹出来。
     */
    static void preOrderIter(BinaryTree.TreeNode root) {
        if (root == null) {
            return;
        }
        Deque<BinaryTree.TreeNode> stack = new ArrayDeque<>();  // ArrayDeque 当栈用（比老的 Stack 快）
        stack.push(root);

        while (!stack.isEmpty()) {
            BinaryTree.TreeNode cur = stack.pop();    // 弹出来就打印（前序：一进节点就处理）
            System.out.print(cur.data + " ");

            if (cur.right != null) {                  // 右先压 → 后出
                stack.push(cur.right);
            }
            if (cur.left != null) {                   // 左后压 → 先出
                stack.push(cur.left);
            }
        }
    }

    /**
     * ④ 中序 · 非递归：左 → 根 → 右（★本关最难的一格）
     *    口诀：**一路向左压栈，弹出来就访问，然后转向它的右子树**
     *
     *    为什么循环条件要写两个？
     *      - cur != null      ：还在往下走的路上（左边还没压完）
     *      - !stack.isEmpty() ：走到底了，但栈里还有"回头要处理"的祖先
     *      只写 stack 非空 → 一开始栈是空的，直接结束，什么都不打印
     */
    static void inOrderIter(BinaryTree.TreeNode root) {
        Deque<BinaryTree.TreeNode> stack = new ArrayDeque<>();
        BinaryTree.TreeNode cur = root;

        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {                     // 一路向左：把路上的节点全压起来
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();                        // 左边走到头了 → 弹出（此时它的左边已全部处理完）
            System.out.print(cur.data + " ");         // 访问根
            cur = cur.right;                          // 转向右子树（右子树可能为 null，下一轮就靠栈里存的祖先）
        }
    }

    /**
     * ⑤ 后序 · 非递归：左 → 右 → 根（用**双栈**）
     *    思路：先用"根 → 右 → 左"的顺序走一遍，把节点依次压进 stack2；
     *          stack2 再出栈，顺序正好反过来 → 左 → 右 → 根。
     *    （另一条路是"单栈 + lastVisited 标记"，但双栈这条更好记，先掌握它）
     */
    static void postOrderIter(BinaryTree.TreeNode root) {
        if (root == null) {
            return;
        }
        Deque<BinaryTree.TreeNode> stack1 = new ArrayDeque<>();
        Deque<BinaryTree.TreeNode> stack2 = new ArrayDeque<>();

        stack1.push(root);
        while (!stack1.isEmpty()) {
            BinaryTree.TreeNode cur = stack1.pop();
            stack2.push(cur);                          // 进 stack2 的顺序：根 → 右 → 左

            if (cur.left != null) {                    // 左先压 stack1 → 后出
                stack1.push(cur.left);
            }
            if (cur.right != null) {                   // 右后压 stack1 → 先出
                stack1.push(cur.right);
            }
        }

        while (!stack2.isEmpty()) {
            System.out.print(stack2.pop().data + " "); // 出 stack2 即反转 → 左 右 根
        }
    }

    public static void main(String[] args) {
        BinaryTree.TreeNode t = BinaryTree.build(new int[]{1, 2, 3, 4, -1, 5, 6});
        // 这棵树：      1
        //             /   \
        //            2     3
        //           /     / \
        //          4     5   6

        System.out.println("=== ① 层序（期望 1 2 3 4 5 6）===");
        System.out.print("实际：");
        levelOrder(t);
        System.out.println();

        System.out.println("=== ② 按层分行（期望 1 / 2 3 / 4 5 6 三行）===");
        levelOrderByLine(t);

        System.out.println("=== ③ 非递归 vs 递归：逐字符比对 ===");
        System.out.print("递归   前序：");
        BinaryTree.preorder(t);
        System.out.println();
        System.out.print("非递归 前序：");
        preOrderIter(t);
        System.out.println();

        System.out.print("递归   中序：");
        BinaryTree.inorder(t);
        System.out.println();
        System.out.print("非递归 中序：");
        inOrderIter(t);
        System.out.println();

        System.out.print("递归   后序：");
        BinaryTree.postorder(t);
        System.out.println();
        System.out.print("非递归 后序：");
        postOrderIter(t);
        System.out.println();

        System.out.println("=== ④ 边界：空树（期望：不崩、什么都不打印）===");
        System.out.print("层序：");
        levelOrder(null);
        System.out.print("| 前序：");
        preOrderIter(null);
        System.out.print("| 中序：");
        inOrderIter(null);
        System.out.print("| 后序：");
        postOrderIter(null);
        System.out.println("[没崩]");

        System.out.println("=== ⑤ 边界：单节点 build({7})（期望 四个都是 7）===");
        BinaryTree.TreeNode one = BinaryTree.build(new int[]{7});
        System.out.print("层序：");
        levelOrder(one);
        System.out.print("| 前序：");
        preOrderIter(one);
        System.out.print("| 中序：");
        inOrderIter(one);
        System.out.print("| 后序：");
        postOrderIter(one);
        System.out.println();

        System.out.println("=== ⑥ 边界：只有右孩子 build({1,-1,2})（期望 中序 1 2）===");
        BinaryTree.TreeNode onlyRight = BinaryTree.build(new int[]{1, -1, 2});
        System.out.print("层序：");
        levelOrder(onlyRight);
        System.out.print("| 中序：");
        inOrderIter(onlyRight);
        System.out.println();

        System.out.println("=== ⑦ 左斜树（1←2←3←4，全在左边，4 个节点）===");
        // 层序槽位：1 | 1左=2, 1右=-1 | 2左=3, 2右=-1 | 3左=4, 3右=-1
        BinaryTree.TreeNode leftSkew = BinaryTree.build(new int[]{1, 2, -1, 3, -1, 4, -1});
        System.out.print("前序：");
        preOrderIter(leftSkew);
        System.out.print("| 中序：");
        inOrderIter(leftSkew);
        System.out.print("| 后序：");
        postOrderIter(leftSkew);
        System.out.print("| 节点数：");
        System.out.println(BinaryTree.size(leftSkew));
    }
}
