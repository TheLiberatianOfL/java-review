package DataStructure;

import java.util.PriorityQueue;

/**
 * 🐳 默写题单：哈夫曼树（建树 + 编码）
 * 日期：2026-09-20 · 复习期第 2 周 · 数据结构线（补课日 08:15-10:00 段）
 *
 * 要求（脱稿写，写完跑自测）：
 *  1. Node 内部类：char ch / int weight / Node left, right
 *     —— 叶子节点带字符，内部节点不带（用一个占位符或单独构造器区分）
 *  2. buildTree(...)：把 n 个「字符 + 权重」装进**优先队列（最小堆）**，每次取两个最小的合并，
 *     新节权 = 两者之和，放回队列；直到队列只剩一个 → 那就是根
 *  3. generateCodes(Node root, String path, String[] codes)：
 *     递归下去，**左走加 '0'、右走加 '1'**；到叶子就把 path 记进编码表（`codes[ch] = path`）
 *     ⚠️ **更正 2026-09-20**：原题单写的是 `Map<Character,String>`，但 Map 属 W4 新内容（复习期不碰新概念）
 *        → 改用 `String[] codes = new String[128]`，**下标直接用字符本身**（char 能当整数用：`codes['A'] = "1100"`）
 *  4. encode(String text) / decode(String code)：按编码表编码，按树从根往下走译码
 *
 * 自测期望（main 里跑出来并打印）：
 *  ① 权重 A(5) B(9) C(12) D(13) E(16) F(45)
 *     → 打印每个字符的编码；算出 **WPL（带权路径长度）= Σ 权重 × 编码长度 = 224**（唯一值，用它校验）
 *  ② 前缀码自检：任何一条编码都不是另一条的前缀（两层循环检查，打印 true）
 *  ③ 单字符文本（如 "aaaa"）→ 不崩（思考：树只有一个节点时编码表怎么给？）
 *  ④ "hello world" → encode 后再 decode，必须等于原文
 *
 * ⚠️ 核心考点（拷打会问）：
 *  - 为什么用最小堆？「每次取两个最小值」如果用排序/线性扫描，代价差在哪（堆 O(log n) vs O(n)）
 *  - 为什么哈夫曼编码一定是前缀码？（提示：带字符的只可能是叶子）
 *  - 建树复杂度 O(k log k)、总空间 O(k)；k = 不同字符数，不是文本长度
 *  - 作用：频率高的短码、频率低的长码 → 压缩率从哪来（对比定长编码）
 */

// TODO 2026-09-20：主人默写区 —— 从空类开始写，写完对照上面自测期望验证
public class HuffmanTree {
    static class Node implements Comparable<Node> {
        char ch;            // 只有叶子才有字符；内部节点填 '\0'
        int weight;         // 权值
        Node left, right;   // 左右孩子

        // 叶子用
        Node(char ch, int weight) {
            this.ch = ch;
            this.weight = weight;
        }

        // 内部节点用（没有字符，只有权值和两个孩子）
        Node(int weight, Node left, Node right) {
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node other) {
            return this.weight - other.weight;   // 负数 = 我排前面（我更小）
        }
    }
    public static Node buildTree(char[] chars, int[] weights) {
        // ① new 一个 PriorityQueue<Node>
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        // ② 循环：每个字符建一个叶子节点，offer 进堆
        for (int i = 0; i < chars.length; i++) {
            pq.offer(new Node(chars[i], weights[i]));
        }
        // ③ while (堆.size() > 1) {
        //        取两个最小的：poll() 两次
        //        new 一个内部节点（权 = 两者之和，左=a，右=b）
        //        offer 回堆          ← 别忘了这步！
        //    }
        while(pq.size()>1){
            Node node1 = pq.poll();
            Node node2 = pq.poll();
            Node node3 = new Node(node1.weight + node2.weight, node1, node2);
            pq.offer(node3);
        }
        // ④ return 堆.poll();   // 只剩一个，就是根
        return pq.poll();
    }
    public static void generateCodes(Node node,String path,String[] codes) {
        if(node==null){
            return;
        }
        if(node.left==null&&node.right==null){
            codes[node.ch]=path.isEmpty()?"0":path;
            return;
        }
        generateCodes(node.left,path+"0",codes);
        generateCodes(node.right,path+"1",codes);
    }
    public static String encode(String text, String[] codes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (codes[c] == null) throw new IllegalArgumentException("字符不在表里: " + c);
            sb.append(codes[c]);          // 查表：c 的编码是多少，直接接上去
        }
        return sb.toString();
    }


    public static String decode(String code, Node root) {

        StringBuilder sb = new StringBuilder();
        Node cur = root;
        if (root.left == null && root.right == null) {
            for (int i = 0; i < code.length(); i++) sb.append(root.ch);
            return sb.toString();
        }
        for (int i = 0; i < code.length(); i++) {
            cur = (code.charAt(i) == '0') ? cur.left : cur.right;
            if (cur == null) throw new IllegalArgumentException("非法编码：走空了");
            if (cur.left == null && cur.right == null) {
                sb.append(cur.ch);
                cur = root;
            }
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        Node root = buildTree(new char[]{'a','b'}, new int[]{2,3});
        String[] codes = new String[128];
        generateCodes(root, "", codes);
        System.out.println("a -> " + codes['a']);   // 应该打印 a -> 0
        System.out.println("b -> " + codes['b']);   // 应该打印 b -> 1
    }
}
