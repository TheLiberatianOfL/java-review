package DataStructure;

/**
 * 🐳 默写题单：双端队列（Deque · double-ended queue）
 * 日期：2026-09-14 · 复习期第 2 周 · W2 树模块前置（数据结构手写训练计划 9/14 项①）
 *
 * 讲义对应章节：04-资料\数据结构\报班笔记\1-栈、队列-精要.md（队列的变体部分）
 *
 * 要求（凭记忆写，写完再对照讲义自查）：
 *  1. addFirst(int x)    队头进
 *  2. addLast(int x)     队尾进
 *  3. removeFirst()      队头出（返回并移除）
 *  4. removeLast()       队尾出（返回并移除）
 *  5. peekFirst()        看队头（不移除）
 *  6. peekLast()         看队尾（不移除）
 *  7. isEmpty() / size() 判空 / 元素个数
 *
 * 选型说明（写代码前先在注释里回答一句）：
 *  - 环形数组 还是 双向链表？为什么？（提示：环形数组随机访问 O(1) 但扩容要搬；
 *    双向链表两端都 O(1) 但每个节点多两个引用）
 *  - 若选环形数组：front 往前挪要用 (front - 1 + capacity) % capacity，
 *    直接 front-1 会变成负数下标 —— 这是本题最容易翻车的地方
 *
 * 自测期望（自己写 main 打印验证，别只看代码"应该对"）：
 *  addLast 1, addLast 2, addFirst 0
 *    → peekFirst 0 · peekLast 2 · size 3
 *  removeFirst → 0 · removeLast → 2 · size 1
 *  removeFirst → 1 · isEmpty → true
 *
 * 边界用例（必跑）：
 *  空队列调 removeFirst / removeLast / peekFirst → 不能抛未处理的崩溃，要有明确返回或提示
 *  容量 1 的队列：addFirst 再 removeLast 应拿到同一个值
 *  假溢出：环形数组下反复 add/remove 绕圈 ≥ capacity 次，元素顺序不能乱
 *
 * ⚠️ 核心考点：
 *  - 双端队列两端都能出入 —— 它同时是栈（只用 addFirst/removeFirst）也是队列（addLast/removeFirst）
 *  - 环形数组的队空 / 队满判定：用 count 计数最稳（别用 front == rear 判空又判满，会打架）
 *  - 越界优先判空再取数：所有 remove/peek 开头先 isEmpty()
 */

// TODO 2026-09-14：主人默写区 —— 从空类开始写，写完跑上面的自测期望 + 边界用例

public class DequeByArray {
    // 建议字段：int[] data; int front, rear, count, capacity;
    // 构造：DequeByArray(int capacity)
    // 按"要求"里的七个方法逐个补全，最后自己写 main 把自测期望跑一遍
    int[] data;
    int front;
    int rear;
    int count;
    int capacity;
     public DequeByArray(int capacity) {
         this.capacity = capacity;
         data = new int[capacity];
         front = 0;
         rear = -1;
         count = 0;
     }
     public boolean isEmpty() {
         return count == 0;
     }
     public boolean isFull() {
         return count == capacity;
     }
     public void addFirst(int data) {
         if(isFull()){
             System.out.println("Queue is full");
             return;
         }
         if(isEmpty()){
             rear = front;
         }
         front =(front -1+capacity)%capacity;
         this.data[front]=data;
         count++;
     }
     public void addLast(int data) {
         if(isFull()){
             System.out.println("Queue is full");
             return;
         }
         rear=(rear+1)%capacity;
         this.data[rear]=data;
         count++;
     }
     public int removeFirst() {
         if (isEmpty()) {
             System.out.println("Queue is empty");
             return -1;
         }
         int result = data[front];
         front = (front + 1) % capacity;
         count--;
         return result;
     }
     public int removeLast() {
         if (isEmpty()){
             System.out.println("Queue is empty");
             return -1;
         }
         int result = data[rear];
         rear=(rear-1+capacity)%capacity;
         count--;
         return result;
     }
     public int peekFirst() {
         if (isEmpty()) {
             System.out.println("Queue is empty");
             return -1;
         }
         return data[front];
     }
     public int peekLast() {
         if (isEmpty()) {
             System.out.println("Queue is empty");
             return -1;
         }
         return data[rear];
     }
     public int size() {
         return count;
     }
}
