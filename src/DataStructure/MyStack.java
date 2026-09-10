package DataStructure;

/**
 * 🐳 默写题单：栈（Stack）
 * 日期：2026-09-10 · 复习期第1周 · 数据结构课冗余补欠账（9/9 欠账）
 *
 * 要求（凭记忆写，写完再对照 shujujiegou1 的 xianxingbiao/stack*.java 自查）：
 *  1. 选一种底层实现（数组 或 链表），说明为什么选它
 *  2. push(int x)     入栈（压栈）
 *  3. pop()           出栈（弹栈，返回栈顶并移除）
 *  4. peek()          看栈顶（不移除）
 *  5. isEmpty()       判空
 *  6. size()          栈内元素个数（有 length 字段时记得维护）
 *
 * 自测期望：
 *  push 1 2 3 → peek 3 → pop 3 → pop 2 → isEmpty false → pop 1 → isEmpty true
 *
 * ⚠️ 核心考点：后进先出（LIFO）；数组栈注意栈顶指针指向"下一个空位"还是"栈顶元素"；
 *    栈满/栈空边界别越界。
 */

// TODO 2026-09-10：主人默写区 —— 从空类开始写，写完再对照自查

public class MyStack {
    private int[] data;
    private int top;
    private int length;
    public MyStack(int length) {
        this.length = length;
        this.data = new int[length];
        this.top =0;

    }
    public void push(int value) {
        if (top==length) {
            System.out.println("Stack is full");
            return;
        }
        data[top] = value;
        top++;

    }
    public int pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return -1;
        }
        int value = data[top-1];
        top--;
        return value;
    }
    public int peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return -1;
        }
        return data[top-1];
    }
    public boolean isEmpty() {
        if (top == 0) {
            return true;
        }
        return false;
    }
    public int size() {
        return top;
    }

    public static void main(String[] args) {
        MyStack stack = new MyStack(10);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        int n=stack.peek();
        System.out.println(n);
        n=stack.pop();
        System.out.println(n);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }

}
