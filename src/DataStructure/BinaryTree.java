package DataStructure;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 🐳 默写题单：二叉树建树 + 三种递归遍历
 * 日期：2026-09-14 · 复习期第 2 周 · W2 树模块第 1 步（数据结构手写训练计划 9/14 项②）
 *
 * ═══════════ 先把三个概念分清（2026-09-14 补 · 防歧义）═══════════
 * 1) 【定义层】二叉树是「树」的**特例**：每个结点最多 2 个孩子，且**左右有严格之分**。
 *    —— "有左右"是二叉树定义的一部分，不是可选项；一般树没有左右之分（无序树）。
 * 2) 【存储层】本类用**链式存储**：TreeNode { val, left, right }。
 *    二叉树的另一种存储叫**顺序存储**（数组，靠编号公式 左=2i / 右=2i+1 / 父=i/2），
 *    但那**只适合完全二叉树**，且它说的是"用数组装树"——与本类的 int[] 参数**不是一回事**。
 * 3) 【输入层】build(int[] levelOrder) 里的 int[] **只是建树的输入素材**（一个层序序列），
 *    建完即弃，树里**没有数组**。类比：菜谱（数组）不是菜（TreeNode 串起来的树）。
 *
 *    ⚠️ 所以：**"用数组" ≠ "顺序存储"**。前者是输入格式，后者是存储结构。
 *    ⚠️ 一般树**不能**用层序数组建树——它没有"左右槽位"，一串数字定不出唯一形状；
 *       一般树靠广义表 A(B,C) 或"孩子兄弟表示法"表示。本项只做二叉树，不涉及多叉树。
 * ═══════════════════════════════════════════════════════════════
 *
 * 讲义对应章节：04-资料\数据结构\报班笔记\2-树、二叉树、线索二叉树-精要.md
 *                §6 二叉树的存储 · §7 遍历（★★重点）
 * 依赖顺序（主人 2026-09-13 定，不许跳枝）：
 *   建树 → 遍历(递归 → 非递归 → 层序) → 线索化 → BST 插入/查找 → BST 删除 → AVL → 哈夫曼/并查集
 *
 * ── 本次：建树 + 递归版三种遍历 + size/height（2026-09-16 由鲸鱼娘陪练补齐）──
 *
 * 要求（凭记忆写）：
 *  1. 静态内部类 TreeNode：int val; TreeNode left, right;（构造器带 val）
 *  2. static TreeNode build(int[] levelOrder)
 *       用**层序数组作为输入素材**，构建一棵**链式存储**的二叉树：
 *         - 数组元素按从上到下、从左到右排列
 *         - 用 -1 表示「空位置」（本仓库约定，因为题里节点值都是正整数）
 *         - 空数组 或 null → 返回 null（空树）
 *       实现思路（自己先想，想不出再看这行）：用一个队列存"已建好但孩子还没接上"的节点，
 *      每从数组取一个值，就接到队头节点的左孩子、下一个值接到右孩子，接完出队。
 *      ⚠️ 取到 -1 时"不建节点"，但仍要占掉这个位置（这是最容易错的一格）
 *  3. static void preOrder(TreeNode root)   前序：根 → 左 → 右
 *  4. static void inOrder(TreeNode root)    中序：左 → 根 → 右
 *  5. static void postOrder(TreeNode root)  后序：左 → 右 → 根
 *       三个都用递归实现，打印格式统一：值 + 空格，如 "1 2 4 3 "（末尾多个空格不算错）
 *  6. static int size(TreeNode root)        节点总数（递归）
 *  7. static int height(TreeNode root)      树高（空树 0、单节点 1）
 *
 * 自测期望：
 *  ⚠️ 刻意不给完整序列 —— 树形先自己画出来（这张"图"就是本题真正的产出）：
 *    build(new int[]{1, 2, 3, 4, -1, 5, 6})  →
 *      根是 1；1 的左 2、右 3；2 的左 4、右空；3 的左 5、右 6
 *    → 先在纸上画出这棵树，再自己推出三种遍历序列，写完用代码核对（对不上说明画错或写错）
 *    → 这张树应满足：size == 6 · height == 3
 *
 *  边界用例（必跑）：
 *    build(null) / build(new int[]{}) → 返回 null；size(null) == 0 · height(null) == 0
 *      （⚠️ 对空树调遍历会 NPE —— 想清楚：是让调用方保证非空，还是在方法里判 null）
 *    build(new int[]{7}) → 单节点：三种遍历都只输出 7；size 1 · height 1
 *    build(new int[]{1, -1, 2}) → 1 无左孩子、右孩子是 2：中序输出 1 再 2（别输出成 2 1）
 *
 * ⚠️ 核心考点：
 *  - 递归三要素：终止条件（root == null 返回）· 单层逻辑 · 相信递归（别在脑子里展开整个栈）
 *  - 前/中/后序的差别**只在"访问根"这一行放的位置**，三份代码结构完全一样
 *  - 层序数组建树：-1 必须占位不建节点 —— 丢掉它会让后面所有节点错位
 *
 * ── 2026-09-16 陪练版：主人当日确认「集合 API 不会 + 思路不清 + 要手把手」，以下实现由鲸鱼娘逐行讲解后落盘 ──
 */

public class BinaryTree {
    // ① 节点：一个值 + 左指针 + 右指针（链式存储的全部内容）
    static class TreeNode {
        int data;
        TreeNode left;
        TreeNode right;

        public TreeNode(int data) {
            this.data = data;
            this.left = null;   // 不写也是 null（成员变量有默认值），写出来是为了"看得见"
            this.right = null;
        }
    }

    /**
     * ② 层序数组 → 链式二叉树
     *    -1 表示空位置：**不建节点，但仍占掉数组里这一格**
     */
    static public TreeNode build(int[] levelOrder) {
        // 2.1 空数组 / null → 空树（空树的"代表值"就是 null）
        if (levelOrder == null || levelOrder.length == 0) {
            return null;
        }

        // 2.2 层序的第一个元素就是根
        TreeNode root = new TreeNode(levelOrder[0]);

        // 2.3 队列：存「已经建好、但孩子还没接上」的节点（等着领孩子）
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        // 2.4 i = 下一个要消费的数组下标（根已经用掉 [0] 了，所以从 1 开始）
        int i = 1;
        while (i < levelOrder.length && !queue.isEmpty()) {
            TreeNode cur = queue.poll();      // 取出一个"等着接孩子"的节点

            // 2.5 先接左孩子
            if (levelOrder[i] != -1) {
                cur.left = new TreeNode(levelOrder[i]);
                queue.offer(cur.left);        // 新节点也要排队，将来接它自己的孩子
            }
            i++;                              // ⚠️ 不管是不是 -1，i 都必须前进（这就是"占位"）

            // 2.6 再接右孩子（要先确认数组还有元素——长度可能正好卡在左孩子用完）
            if (i < levelOrder.length) {
                if (levelOrder[i] != -1) {
                    cur.right = new TreeNode(levelOrder[i]);
                    queue.offer(cur.right);
                }
                i++;
            }
        }
        return root;
    }

    // ③ 前序：根 → 左 → 右
    static public void preorder(TreeNode root) {
        if (root == null) {
            return;                            // 终止条件（同时是空树保护：不会 NPE）
        }
        System.out.print(root.data + " ");     // 访问根
        preorder(root.left);                   // 递归左
        preorder(root.right);                  // 递归右
    }

    // ④ 中序：左 → 根 → 右（与前序的差别只在"访问根"这一行的位置）
    static public void inorder(TreeNode root) {
        if (root == null) {
            return;
        }
        inorder(root.left);
        System.out.print(root.data + " ");
        inorder(root.right);
    }

    // ⑤ 后序：左 → 右 → 根（访问根放最后）
    static public void postorder(TreeNode root) {
        if (root == null) {
            return;
        }
        postorder(root.left);                  // 先递归完左
        postorder(root.right);                 // 再递归完右
        System.out.print(root.data + " ");     // 最后才打印根
    }

    // ⑥ 节点总数 = 我自己(1) + 左子树的 + 右子树的
    static public int size(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + size(root.left) + size(root.right);
    }

    // ⑦ 树高 = 1 + max(左子树高, 右子树高)；空树 0、单节点 1
    static public int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(height(root.left), height(root.right));
    }

    public static void main(String[] args) {
        System.out.println("=== 主用例 build({1,2,3,4,-1,5,6})，判据 size==6 · height==3 ===");
        TreeNode t = build(new int[]{1, 2, 3, 4, -1, 5, 6});
        System.out.println("size   = " + size(t) + "（期望 6）");
        System.out.println("height = " + height(t) + "（期望 3）");

        System.out.print("前序（期望 1 2 4 3 5 6）：");
        preorder(t);
        System.out.println();

        System.out.print("中序（期望 4 2 1 5 3 6）：");
        inorder(t);
        System.out.println();

        System.out.print("后序（期望 4 2 5 6 3 1）：");
        postorder(t);
        System.out.println();

        System.out.println("=== 边界：空树 ===");
        System.out.println("build(null) == null → " + (build(null) == null));
        System.out.println("build({}) == null → " + (build(new int[]{}) == null));
        System.out.println("size(null) = " + size(null) + "（期望 0）");
        System.out.println("height(null) = " + height(null) + "（期望 0）");
        System.out.print("对空树调遍历（期望不崩、什么都不打印）：");
        preorder(null);
        inorder(null);
        postorder(null);
        System.out.println("[没崩]");

        System.out.println("=== 边界：单节点 build({7}) ===");
        TreeNode one = build(new int[]{7});
        System.out.print("前序（期望 7）：");
        preorder(one);
        System.out.println();
        System.out.println("size = " + size(one) + " · height = " + height(one) + "（期望 1 · 1）");

        System.out.println("=== 边界：只有右孩子 build({1,-1,2}) ===");
        TreeNode onlyRight = build(new int[]{1, -1, 2});
        System.out.print("中序（期望 1 2；输出 2 1 就是右孩子接错了）：");
        inorder(onlyRight);
        System.out.println();
        System.out.print("形状检查：根 1 的左应为 null → ");
        System.out.println("root.left == null → " + (onlyRight.left == null)
                + " · root.right.data = " + onlyRight.right.data);
    }
}
