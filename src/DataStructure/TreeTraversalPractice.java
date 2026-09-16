package DataStructure;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 🐳 渐隐练习（faded worked example）：非递归遍历 · 只留「中序」和「后序」
 * 日期：2026-09-16 · 复习期第 2 周 · 承接 TreeTraversalIterative（样例期）
 *
 * ── 这是什么、为什么这么设计 ──
 *  学习科学里，新手阶段"看完整样例"效率最高（样例效应），但**看懂了 ≠ 会写**（流畅性错觉）。
 *  所以样例之后必须紧跟两步：① 渐隐（补关键部分）② 闭卷重写。
 *  本文件就是第 ① 步：**前序已写好当样例，中序和后序留空给你补**。
 *
 * ── 怎么用 ──
 *  1. **先别翻 `TreeTraversalIterative.java`**（那里有完整答案）——先自己写，写完再对。
 *  2. 照着 `preOrderIter` 的**结构**（判空 → 建栈 → 入栈 → while 循环 → 弹/压）来写下面两个。
 *  3. 写完跑 main：判据是「非递归输出 == 递归输出」，逐字符一致。
 *
 * ── 写完自己问三句（口述拷打会问）──
 *  · 中序：外层 while 为什么必须写**两个**条件？只写 `!stack.isEmpty()` 会怎样？
 *  · 中序：栈里存的到底是什么？（一句话）
 *  · 后序：stack1 压孩子时，**先压左还是先压右**？为什么？
 *
 * 编译运行（在本文件目录下）：
 *   javac -encoding UTF-8 -d ..\..\out DataStructure\BinaryTree.java DataStructure\TreeTraversalPractice.java
 *   java -Dfile.encoding=UTF-8 -cp ..\..\out DataStructure.TreeTraversalPractice
 */

public class TreeTraversalPractice {

    /**
     * ✅ 样例（已写好）：前序非递归 —— 根 → 左 → 右
     *    结构：判空 → 建栈 → 根入栈 → while(栈非空) { 弹一个打印；先压右、再压左 }
     */
    static void preOrderIter(BinaryTree.TreeNode root) {
        if (root == null) {
            return;
        }
        Deque<BinaryTree.TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            BinaryTree.TreeNode cur = stack.pop();
            System.out.print(cur.data + " ");

            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }

    /**
     * ✍️ 你来写 ①：中序非递归 —— 左 → 根 → 右
     *
     *  步骤提示（按这个顺序写，别跳）：
     *   1) 建一个栈，再定义一个指针 cur，让它从 root 开始（**注意：中序不用先把 root 压进去**）
     *   2) 外层循环：`while (cur != null || !stack.isEmpty())` —— 想清楚为什么两个条件都要
     *   3) 内层循环：**一路向左**，每经过一个节点就压栈，直到 cur == null
     *   4) 左边到底了 → 弹出栈顶，**打印它**
     *   5) 让 cur 转向刚才那个节点的**右子树**，回到第 2 步
     *
     *  写完后自检：对空树（null）不能崩。
     */
    static void inOrderIter(BinaryTree.TreeNode root) {
        // TODO 你来写（大约 8 行）
        Deque<BinaryTree.TreeNode> stack = new ArrayDeque<>();
        BinaryTree.TreeNode cur = root;
        while (!stack.isEmpty()||cur != null) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            System.out.print(cur.data + " ");
            cur = cur.right;
        }

    }

    /**
     * ✍️ 你来写 ②：后序非递归（双栈法）—— 左 → 右 → 根
     *
     *  思路回顾：不硬判断"根的右子树弄完没有"，而是——
     *   第一阶段：按「根 → 右 → 左」走一遍，弹出来的节点都压进 stack2
     *   第二阶段：stack2 整体出栈打印 → 顺序反转 → 正好是「左 → 右 → 根」
     *
     *  步骤提示：
     *   1) 建 stack1、stack2 两个栈；判空返回
     *   2) stack1.push(root)
     *   3) while (stack1 非空) { cur = stack1.pop(); stack2.push(cur);
     *       再往 stack1 压 cur 的孩子 —— ⚠️ 先压左还是先压右？想清楚，这决定了 stack1 的弹出顺序 }
     *   4) while (stack2 非空) { 弹出并打印 }
     *
     *  ⚠️ 最容易错的一格：第 3 步里"压孩子的顺序"。stack1 要弹出「右」在前，
     *     所以「右」必须**后**压进去（LIFO）。
     *
     *  写完后自检：对空树（null）不能崩。
     */
    static void postOrderIter(BinaryTree.TreeNode root) {
        // TODO 你来写（大约 12 行）
        if (root == null) {
            return ;
        }
        Deque<BinaryTree.TreeNode> stack1 = new ArrayDeque<>();
        stack1.push(root);
        Deque<BinaryTree.TreeNode> stack2 = new ArrayDeque<>();
        BinaryTree.TreeNode cur ;
        while(!stack1.isEmpty()) {
            cur = stack1.pop();
            stack2.push(cur);
            if (cur.left != null) {
                stack1.push(cur.left);
            }
            if (cur.right != null) {
                stack1.push(cur.right);
            }
        }
        while (!stack2.isEmpty()) {
            cur = stack2.pop();
            System.out.print(cur.data + " ");
        }


    }

    public static void main(String[] args) {
        BinaryTree.TreeNode t = BinaryTree.build(new int[]{1, 2, 3, 4, -1, 5, 6});
        //        1
        //      /   \
        //     2     3
        //    /     / \
        //   4     5   6
        //  递归 中序：4 2 1 5 3 6   递归 后序：4 2 5 6 3 1

        System.out.println("=== 判据：非递归输出必须与递归版逐字符一致 ===");

        System.out.print("递归   中序：");
        BinaryTree.inorder(t);
        System.out.println();
        System.out.print("非递归 中序：");
        inOrderIter(t);                    // ← 写完这行才有输出
        System.out.println();

        System.out.print("递归   后序：");
        BinaryTree.postorder(t);
        System.out.println();
        System.out.print("非递归 后序：");
        postOrderIter(t);                  // ← 写完这行才有输出
        System.out.println();

        System.out.println("=== 边界：空树不能崩 ===");
        System.out.print("空树中序：");
        inOrderIter(null);
        System.out.print("| 空树后序：");
        postOrderIter(null);
        System.out.println("[没崩]");

        System.out.println("=== 进阶（可选）：换成只有右孩子的那棵 build({1,-1,2}) ===");
        BinaryTree.TreeNode onlyRight = BinaryTree.build(new int[]{1, -1, 2});
        System.out.print("递归   后序：");
        BinaryTree.postorder(onlyRight);
        System.out.println();
        System.out.print("非递归 后序：");
        postOrderIter(onlyRight);
        System.out.println();
    }
}
