package Algorithm;

import java.util.Stack;

/**
 * 🐳 LeetCode 20 · 有效的括号（热题100 在列）· 栈（LIFO）版
 * 日期：2026-09-10 · 复习期第 1 周 · 力扣 AC（103/103 通过）
 * 提交：TheLiberatianOfL · 17:36 · 用时 3ms（击败 80.10%）/ 内存 42.56MB
 * 说明：方法体 = 主人 AC 提交原版（仅整理缩进与格式），未改逻辑。
 *
 * 思路：括号匹配 = 栈的天然场景。
 *   ① 遇左括号 ( [ {  → push 进栈（等一个"配对的右括号"来消）
 *   ② 遇右括号 → 先看栈顶是不是它对应的左括号：
 *        - 栈空（没得配）或栈顶不配对 → 直接 false
 *        - 配对 → pop 掉栈顶（这一对消掉了）
 *   ③ 全部字符过完 → 栈空 = 全配对 true；栈非空 = 有左括号没被消掉 false
 *
 * 为什么用栈：括号必须"后出现的先闭合"（([)] 非法），这正是 LIFO 的语义；
 *   栈里只放左括号，右括号永远不入栈。
 *
 * 易错点（2026-09-10 主人亲踩，全部记录在案）：
 *   ① 判空必须在 peek 之前 —— `!stack.isEmpty() && stack.peek() == '('`：
 *      && 短路，左边 false 时右边不执行，才不会对空栈 peek 抛异常。
 *   ② pop 写成 poll —— 在 ArrayDeque 上 poll() 与 pop() 都取头部，能跑通，
 *      但 poll 属"队列词汇"、空集合返回 null（自动拆箱即 NPE）；栈题一律 push/pop/peek。
 *   ③ 引号 —— '(' 是 char（基本类型），"(" 是 String（对象），混用编译不过；
 *      char 比较用 ==，String 比较必须用 equals。
 */
public class LC20_ValidParentheses {

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();   // 栈里只放左括号

        for (char c : s.toCharArray()) {                   // 增强 for：把字符串拆成字符挨个过
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);                             // 左括号直接进栈，等配对的右括号
            } else if (c == ')') {
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();                           // 配对成功 → 消掉栈顶那个左括号
                } else {
                    return false;                          // 栈空（多余右括号）或栈顶不配对
                }
            } else if (c == ']') {
                if (!stack.isEmpty() && stack.peek() == '[') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (c == '}') {
                if (!stack.isEmpty() && stack.peek() == '{') {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }

        return stack.isEmpty();                            // 空栈 = 每个左括号都被配掉了
    }

    /*
     * 🚀 优化思路（更优解法 · 本题结构上已是骨架最优，能优化的只有写法）
     * ----------------------------------------
     * 复杂度：时间 O(n)（每个字符必须看一次，无法更快）/ 空间 O(n)（最坏情况全是左括号）。
     *
     * ① 用 Map 配对表把三个 else if 压成一段（W2 集合框架学完回来重写）
     *    当前写法的主要缺点是"三段几乎一样的代码"（重复度高、加一种括号就要复制一段）：
     *
     *    Map<Character, Character> pairs = new HashMap<>();
     *    pairs.put(')', '(');
     *    pairs.put(']', '[');
     *    pairs.put('}', '{');
     *    for (char c : s.toCharArray()) {
     *        if (pairs.containsKey(c)) {                    // 右括号
     *            if (stack.isEmpty() || stack.peek() != pairs.get(c)) return false;
     *            stack.pop();
     *        } else {
     *            stack.push(c);                             // 左括号
     *        }
     *    }
     *    return stack.isEmpty();
     *    💡 HashMap / Map 属 W2 集合框架内容——现在先看懂"配对表"这个想法，W2 亲手写一遍。
     *
     * ② Stack 换成 Deque（JDK 官方推荐写法）
     *    Deque<Character> stack = new ArrayDeque<>();
     *    原因：Stack 是 JDK 1.0 遗留类，继承 Vector，每个方法都带 synchronized 锁
     *    （单线程刷题纯属白付开销）；且它暴露了 get(i) 随机访问、能插中间，
     *    破坏了"栈只能动栈顶"的语义约束。ArrayDeque 只暴露双端操作，更快更纯净。
     *    本文件保留主人 AC 时的 Stack 原貌 —— 方法名完全相同（push/pop/peek/isEmpty），
     *    换过去零成本；面试被问"Java 怎么实现栈"，答 ArrayDeque 是标准加分项。
     *
     * ③ 另辟蹊径的 replace 写法（能 AC，但不推荐）
     *    反复把 "()"、"[]"、"{}" 替换成空串，最后看是否为空串。
     *    缺点：每轮替换都要重建字符串（O(n²)），且交叉错配如 "([)]" 需要多轮才暴露，
     *    远不如栈"一遍扫描 O(n)"干净。
     */

    public static void main(String[] args) {
        // 用例①（主用例）：三种括号各自成对 → true
        System.out.println("用例① " + isValid("()[]{}") + "  期望 true");
        // 用例②（坑用例·类型不匹配）：圆括号配方括号 → false
        System.out.println("用例② " + isValid("(]") + "  期望 false");
        // 用例③（坑用例·交叉嵌套）：([)] 顺序错 → false（栈的核心考点）
        System.out.println("用例③ " + isValid("([)]") + "  期望 false");
        // 用例④（正例·正常嵌套）：{[()]} → true
        System.out.println("用例④ " + isValid("{[()]}") + "  期望 true");
        // 用例⑤（边界·空串）：没有括号 → true
        System.out.println("用例⑤ " + isValid("") + "  期望 true");
        // 用例⑥（边界·只剩左括号）：遍历完栈非空 → false
        System.out.println("用例⑥ " + isValid("(") + "  期望 false");
        // 用例⑦（边界·只剩右括号）：必须靠判空挡住，否则空栈 peek 崩 → false
        System.out.println("用例⑦ " + isValid("]") + "  期望 false");
    }
}
