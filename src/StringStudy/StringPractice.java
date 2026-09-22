package StringStudy;

import java.util.Scanner;

/**
 * 题单 · 字符串与 StringBuilder（2026-09-22 · W3 周二）
 *
 * 本段目标（一句话）：**能讲清"为什么循环里用 String 拼字符串慢"**。
 * 判据：不是"看过"，而是能当场说出 ① 慢在哪一步 ② 为什么是 O(n²) ③ 换谁。
 *
 * 规则：一次只写一题，写完就跑一次；预测先写进注释，再跑，对不上就查为什么。
 * ⚠️ 本文件只给「题目要求 + 期望输出」，答案自己写。
 */
public class StringPractice {

    /*
     * 【题 1】常量池 vs 堆（先写预测，再跑）
     *
     *   String a = "a";
     *   String b = new String("a");
     *   String c = "a";
     *
     *   预测这四行的输出，写在右边注释里：
     *   System.out.println(a == b);          // 预测：____
     *   System.out.println(a == c);          // 预测：____
     *   System.out.println(a.equals(b));     // 预测：____
     *   System.out.println(b.intern() == a); // 预测：____
     *
     *   期望：四个结果里有两个 true、两个 false（不对就说明有一个概念没通）
     */

    /*
     * 【题 2】编译期折叠 vs 运行时拼接（先写预测，再跑）
     *
     *   String s1 = "超用心";
     *   String s2 = "在线教育";
     *
     *   System.out.println((s1 + s2) == "超用心在线教育");            // 预测：____
     *   System.out.println((s1 + s2).intern() == "超用心在线教育");   // 预测：____
     *   System.out.println(("超用心" + "在线教育") == "超用心在线教育"); // 预测：____
     *
     *   期望：第三个和第一行结果**不同**（为什么不同？写一句理由在注释里）
     */

    /*
     * 【题 3】substring 计数
     *
     *   求 "ababababababababa" 中 "aba" 出现了几次。
     *   要求：用 substring(i, i + 3) + equals
     *
     *   期望输出：7
     *   ⚠️ 提示：循环上界是 length() - 3，不是 length()
     *      写之前先答一句：为什么不能直接 i < s.length()？
     */

    /*
     * 【题 4】StringBuilder 做金融数字
     *
     *   把 10005.25 变成 "10,005.25"
     *   要求：① 先 indexOf(".") 定位小数点
     *        ② 从小数点左边每 3 位 insert(",") 一次
     *        ③ 用 StringBuilder 的 insert，最后 toString()
     *
     *   期望输出：10,005.25
     *   ⚠️ 提示：正着插下标会一直变，想想从哪边开始插下标才不用重算
     */

    /*
     * 【题 5】性能实测（本段的核心，务必真跑）
     *
     *   两段代码各跑一次，各计时一次：
     *     ① String  ：String str = ""; 循环 5 万次 str += i;
     *     ② StringBuilder：循环 5 万次 sb.append(i);
     *
     *   要求：用 long t1 = System.currentTimeMillis(); ... long t2 = ... 取差值
     *   期望量级：① 几百毫秒  ② 几毫秒  —— 差**百倍**
     *
     *   👉 把两个实测数字抄进下面注释，并回答一句：为什么差这么多？
     *   实测结果：String = ____ ms，StringBuilder = ____ ms
     *   理由：____
     */

    /*
     * 【题 6】口述题（不写代码，用嘴答）
     *
     *   ① String 拼接慢，慢在**哪一步**？（提示：不是"字符串长"，是某个动作）
     *   ② 为什么总量是 O(n²)？
     *   ③ StringBuilder 为什么是 O(n)？它扩容时做了什么？
     *   ④ 单线程用谁？多线程共享同一个拼接器用谁？为什么？
     */

    public static void main(String[] args) {
        // TODO 一题一题写，写完一题跑一次
        String a = "a";
        String b = new String("a");
        String c = "a";
        System.out.println(a == b);          // 预测：__false__
        System.out.println(a == c);          // 预测：__true__
        System.out.println(a.equals(b));     // 预测：__true__
        System.out.println(b.intern() == a); // 预测：_true___


        String s1 = "超用心";
        String s2 = "在线教育";

        System.out.println((s1 + s2) == "超用心在线教育");            // 预测：__false__
        System.out.println((s1 + s2).intern() == "超用心在线教育");   // 预测：__true__
        System.out.println(("超用心" + "在线教育") == "超用心在线教育"); // 预测：_true___

        //期望：第三个和第一行结果**不同**（为什么不同？写一句理由在注释里）一看编译器能不能提前算出结果
        String str="";
        StringBuilder builder=new StringBuilder();

        long st1=System.currentTimeMillis();
        int i=1;
        while (i<50000){
            str+=i;
            i++;
        }
        long st2=System.currentTimeMillis();
        System.out.println(st2-st1);
        long bt1=System.currentTimeMillis();
        int j=1;
        while (j<50000){
            builder.append(j);
            j++;
        }
        long bt2=System.currentTimeMillis();
        System.out.println(bt2-bt1);


    }
}
