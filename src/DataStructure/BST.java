package DataStructure;

/**
 * 🐳 默写题单：二叉搜索树（BST）· 插入 + 查找
 * 日期：2026-09-11 · 复习期第1周 · 周五场（20:30 档）
 *
 * 要求（脱稿写，写完再对照课件/资料自查）：
 *  1. 定义节点 Node：值 int value · 左子 left · 右子 right
 *  2. insert(int value)  插入：比当前小往左走、比当前大往右走，碰到空位挂上；
 *                        ⚠️ 重复值怎么处理要自己定好（丢弃 / 计数 / 往右挂）并在注释里说明理由
 *  3. search(int value)  查找：命中返回 true（或返回节点），否则按同样规则继续走，走到 null = 没找到
 *  4. 选做：inorder()    中序遍历打印（BST 的中序一定升序 —— 用它验证树建对了没）
 *
 * 自测期望（main 里跑出来）：
 *  依次插入 8 3 10 1 6 14 4 7 13
 *  → 中序遍历应输出：1 3 4 6 7 8 10 13 14（升序 = 树结构正确）
 *  → search(6) true · search(14) true · search(9) false
 *
 * ⚠️ 核心考点：
 *   - BST 的"有序性"是靠插入规则维持的，不是靠排序
 *   - 边界：root 为 null（空树插入 / 空树查找）必须处理；插入时别把 parent 指针丢了
 *   - 递归写法要想清"返回什么"（返回新子树根 vs 直接原地改）
 */

// TODO 2026-09-11：主人默写区 —— 从空类开始写，写完对照自测期望验证
public  class BST {
    public static class Node {
        int value;
        Node left, right;

        public Node(int value) {
            this.value = value;
            left = right = null;
        }
    }

    static Node root;

    public BST(Node root) {
        this.root = root;
    }

    public static void insert(int value) {
        if (root == null) {
            root = new Node(value);
            return;
        }
        Node n = root;
        while (n != null) {
            if (value < n.value) {
                if (n.left == null) {
                    n.left = new Node(value);
                    return;
                } else {
                    n = n.left;
                }
            } else if (value > n.value) {
                if (n.right == null) {
                    n.right = new Node(value);
                    return;
                } else
                    n = n.right;
            } else {
                System.out.println("已经存在");
                return;
            }
        }
    }
    public static boolean delete(int value) {
        if (!search(value)) return false;    // 只在最外层检查一次
        root = delect(root, value);
        return true;
    }
    public static Node delect(Node n, int value) {

        if (n == null) return null;
        if (n.value < value) {
            n.right = delect(n.right, value);
        } else if (n.value > value) {
            n.left = delect(n.left, value);
        } else {
            if (n.left == null) {
                return n.right;
            } else if (n.right == null) {
                return n.left;
            }
            Node ans=min(n.right);
            n.value=ans.value;
            n.right = delect(n.right, ans.value);
        }
        return n;
    }
    public static Node min(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }
    public static boolean search(int value) {
        Node n=root;
        while (n != null) {
            if (n.value == value) {

                return true;
            } else if (n.value > value) {
                n = n.left;
            }
            else if (n.value < value) {
                n = n.right;
            }
        }

        return false;
    }
    public static void search(int value, Node n) {
        while (n != null) {
            if (n.value == value) {
                System.out.println("The value is found in the tree");
                return;
            } else if (n.value > value) {
                n = n.left;
            }
            else if (n.value < value) {
                n = n.right;
            }
        }
        System.out.println("The value is not in the tree");
        return;
    }
    public static void inorder(Node n) {
        if (n == null) {
            return;
        }
        else {
            inorder(n.left);
            System.out.println(n.value);
            inorder(n.right);
        }
    }

    public static void main(String[] args) {
        BST tree= new BST(new Node(8));
        tree.insert(3);
        tree.insert(10);
        tree.insert(1);
        tree.insert(6);
        tree.insert(14);
        tree.insert(4);
        tree.insert(7);
        tree.insert(13);
        tree.search(6, tree.root);
        tree.search(14, tree.root);
        tree.inorder(tree.root);
    }



}