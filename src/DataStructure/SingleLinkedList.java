package DataStructure;

/**
 * 🐳 默写题单：带头结点单链表（增 / 删 / 查）
 * 日期：2026-09-08 · 复习期第1周
 *
 * 要求（凭记忆写，写完再对照 shujujiegou1 的 Linklist.java 自查）：
 *  1. add(int data)       尾插
 *  2. insert(int index, int data)   任意位插入（找前驱是关键）
 *  3. delete(int data)    按值删除（留 prev 跟着走，删完处理尾）
 *  4. find(int data)      按值查找（返回下标或节点）
 *  5. display()           打印链表
 *
 * 自测期望：
 *  add 1 2 3 → display    1 2 3
 *  insert(2, 9) → display 1 2 9 3
 *  delete(2) → display    1 9 3
 *  find(9) → 2（从 1 数）
 */
public class SingleLinkedList {

    private Node head;      // 头结点：不带数据，next 指向首元节点
    private int length = 0; // 链表长度（数据节点个数）

    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // 构造器：创建头结点（关键！head 必须是真实节点，否则一跑就空指针）
    public SingleLinkedList() {
        head = new Node(-1);
    }

    // add：尾插 —— 从头结点走到最后一个节点，接上新节点
    public void add(int data) {
        Node n = new Node(data);
        Node cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = n;
        length++;
    }

    // insert：按下标插入（index 从 0 开始，0 = 插到最前面）
    // 核心：cur 从头结点走 index 步，正好停在"目标位置的前驱"上
    public void insert(int index, int data) {
        if (index < 0 || index > length) {
            System.out.println("插入位置不合法");
            return;
        }
        Node n = new Node(data);
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        n.next = cur.next;   // 新节点先接住后段
        cur.next = n;        // 前驱再接新节点（顺序不能反！）
        length++;
    }

    // delete：按值删除 —— pre 跟着 cur 走，找到后 pre 直接跨过 cur
    public void delete(int data) {
        Node pre = head;
        Node cur = head.next;
        while (cur != null && cur.data != data) {
            pre = cur;
            cur = cur.next;
        }
        if (cur == null) {
            System.out.println("不存在这个值: " + data);
            return;
        }
        pre.next = cur.next;   // 前驱跨过被删节点（Java 自动回收）
        length--;
    }

    // find：按值查找，返回从 1 开始的位置；找不到返回 -1
    public int find(int data) {
        Node cur = head.next;
        int pos = 1;
        while (cur != null) {
            if (cur.data == data) {
                return pos;
            }
            cur = cur.next;
            pos++;
        }
        return -1;
    }

    // display：打印整条链
    public void display() {
        Node cur = head.next;
        while (cur != null) {
            System.out.print(cur.data + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    // main：跑自测
    public static void main(String[] args) {
        SingleLinkedList list = new SingleLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.display();                    // 1 2 3
        list.insert(2, 9);
        list.display();                    // 1 2 9 3
        list.delete(2);
        list.display();                    // 1 9 3
        int pos = list.find(9);
        System.out.println("find(9) 在第 " + pos + " 个");
    }
}
