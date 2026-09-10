package DataStructure;

/**
 * 🐳 默写题单：队列（Queue）
 * 日期：2026-09-10 · 复习期第1周 · 数据结构课冗余补欠账（9/9 欠账）
 *
 * 要求（凭记忆写，写完再对照 shujujiegou1 的 xianxingbiao/queue*.java 自查）：
 *  1. 选一种底层实现（数组环形 或 链表），说明为什么选它
 *  2. offer(int x)    入队（队尾进）
 *  3. poll()          出队（队头出，返回并移除）
 *  4. peek()          看队头（不移除）
 *  5. isEmpty()       判空
 *  6. size()          队内元素个数
 *
 * 自测期望：
 *  offer 1 2 3 → peek 1 → poll 1 → poll 2 → isEmpty false → poll 3 → isEmpty true
 *
 * ⚠️ 核心考点：先进先出（FIFO）；若用数组实现必须想清楚"假溢出"怎么解决
 *    （环形队列 / 取模），队空与队满的判定条件别搞混。
 */

// TODO 2026-09-10：主人默写区 —— 从空类开始写，写完再对照自查

public class MyQueue {
    //选环形数组，因为链式本质上是尾插和头删，没意思
    int[] data;
    int front, rear;
    int capacity;
    int count;
    public MyQueue(int size) {
        this.capacity = size;
        this.data = new int[size];
        this.front = 0;
        this.rear = 0;

    }
    public boolean isEmpty() {
        return (count==0);
    }
    public boolean isFull() {
        return ( count == data.length);
    }
    public void offer(int x) {
        if(isFull()) {
            System.out.println("Queue is full");
            return;
        }
        data[rear] = x;
        rear = (rear+1)%capacity;
        count++;
    }
    public int peek() {
        if(isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }
        return data[front];
    }
    public int poll() {
        if(isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }
        int x = data[front];
        front=(front+1)%capacity;
        count--;
        return x;
    }
    public int size() {
        return count;
    }
    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue(5);
        myQueue.offer(1);
        myQueue.offer(2);
        myQueue.offer(3);
        System.out.println(myQueue.peek());
        System.out.println(myQueue.poll());
        System.out.println(myQueue.poll());
        System.out.println(myQueue.poll());
        System.out.println(myQueue.poll());
    }
}
