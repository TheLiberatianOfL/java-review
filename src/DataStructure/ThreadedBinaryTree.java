package DataStructure;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 🐳 默写题单：线索二叉树（中序线索化）+ 线索树遍历
 * 日期：2026-09-19 · 复习期第2周 · 救火日 ·（补 9/17 顺延项 · 轻量档）
 *
 * 要求（脱稿写，写完再对照资料自查）：
 *  1. 定义节点 ThreadedNode：int value · left · right
 *     + 两个标志位 leftThread / rightThread（boolean）
 *       —— 注释里写清：这两个标志 true 时，对应的 left/right 到底指「真孩子」还是「线索」
 *  2. 建树：先用普通二叉树方式建（可直接复用 BinaryTree 的层序数组建树 · -1 占位）
 *  3. threadNodes() 中序线索化：
 *       需要一个 prev 指针记录「上一个被访问的节点」
 *       ⚠️ 两条线索不是在同一处产生的：左线索在「我」身上、右线索在「前驱」身上 —— 想清这句再动手
 *  4. inorderTraversal() 用线索遍历（**不许用栈、不许用递归**）：
 *       起点 = 中序第一个节点；找后继有两条路，走哪条取决于标志位
 *
 * 自测期望（main 里跑出来）：
 *  层序数组 {4,2,6,1,3,5,7} 建树（无 -1 空位）
 *  → 线索中序遍历输出：1 2 3 4 5 6 7
 *  → 逐节点打印左右标志，能一眼看出哪些指针是线索（例如 1 的右线索 → 2、3 的左线索 → 2）
 *  → 与 BinaryTree 的递归中序结果逐字符一致
 *
 * ⚠️ 核心考点：
 *  - 「线索指向谁」：左线索 → 中序**前驱**；右线索 → 中序**后继**
 *  - 「为什么需要线索」：普通二叉树 n 个节点有 n+1 个空指针被浪费；线索把它们用起来，换来 O(1) 找前驱/后继、省掉栈与递归
 *  - 边界：空树不崩 · 只有一个节点 · 只有左子树 · 只有右子树
 */

// TODO 2026-09-19：主人默写区 —— 从空类开始写，写完对照自测期望验证
public class ThreadedBinaryTree {
    public static class ThreadedNode{
        int value;
        ThreadedNode left;
        ThreadedNode right;
        boolean leftThread;
        boolean rightThread;
        public ThreadedNode(int value) {
            this.value = value;
            this.leftThread = false;
            this.rightThread = false;
            this.left = null;
            this.right = null;
        }
    }
    static ThreadedNode root;
    static ThreadedNode pre;
    public void build(int[] level) {
        root = buildNode(level, 0);
    }

    private ThreadedNode buildNode(int[] level, int i) {
        if (i >= level.length || level[i] == -1) {
            return null;
        }
        ThreadedNode node = new ThreadedNode(level[i]);
        node.left = buildNode(level, 2 * i + 1);    // 左孩子在 2i+1
        node.right = buildNode(level, 2 * i + 2);   // 右孩子在 2i+2
        return node;
    }
    public static void threadNode()  {
        pre =null;
        doThread(root);
    }
    public static void doThread(ThreadedNode node) {
        if (node == null) {
            return;
        }
        doThread(node.left);
        if(node.left==null){
            node.left = pre;
            node.leftThread = true;
        }
        if(pre!=null &&pre.right==null){
           pre.right = node;
           pre.rightThread = true;
        }
        pre = node;
        doThread(node.right);
    }
    public static void inOrderThread() {
        ThreadedNode node = root;
        while (node!= null && !node.leftThread) {
            node = node.left;
        }
        while(node!=null){
            System.out.println(node.value+" ");
            if(node.rightThread){
                node = node.right;
            }else {
                node=node.right;
                while(node!=null &&!node.leftThread){
                    node = node.left;
                }
            }
        }
    }

    public static void main(String[] args) {
        ThreadedBinaryTree tree = new ThreadedBinaryTree();
        tree.build(new int[]{4,2,6,1,3,5,7});
        tree.threadNode();
        tree.inOrderThread();


    }

}
