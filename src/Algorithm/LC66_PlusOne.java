package Algorithm;

/**
 * 🐳 LeetCode 66 · 加一 · 模拟（进位）版
 * 日期：2026-09-09 · 复习期第 1 周 · 力扣 AC（114/114 通过）
 * 提交：TheLiberatianOfL · 12:48 · 用时 0ms（击败 100.00%）/ 内存 42.64MB
 *
 * 思路：模拟"竖式加法"的进位——从最低位（数组末尾）往前 +1；
 *       某位 +1 后 != 0 说明进位停住，直接返回；一路进位到头 = 全 9，数组要变长。
 *
 * 易错点：① 全 9（999+1=1000）位数增加，必须 new 长度+1 的数组，首位写 1；
 *         ② 普通情况（如 129+1=130）只在低位变化，高位原样保留，别整体重算。
 */
public class LC66_PlusOne {

    public static int[] plusOne(int[] digits) {
        // 从最低位往高位模拟进位
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;                 // 当前位 +1
            digits[i] %= 10;             // 取个位：9+1=10 → 0 并进位；其他值不变
            if (digits[i] != 0) {        // 没产生进位 → 高位不用动，直接返回
                return digits;
            }
            // 否则说明这位原来是 9（9+1=10），进位继续往高位传
        }
        // 能走到这说明每一位都是 9：999+1 → 1000，需要更长数组
        int[] res = new int[digits.length + 1];
        res[0] = 1;                      // 最高位置 1，其余位默认 0
        return res;
    }

    /*
     * 🚀 为什么这个写法已经是最优（没有更快的算法，但有更准的直觉）
     * ----------------------------------------
     * 1. 时间复杂度：最坏 O(n)（全 9 一路进位），平均 O(1)——绝大多数数字
     *    最低位 +1 后不进位就返回了，循环只跑一次。模拟题的关键是"提前终止"：
     *    不是 9 的位加完就结束，别机械地扫完整个数组。
     * 2. 空间复杂度：正常情况全程原地改 digits，额外空间 O(1)；
     *    只有"全 9"这一种极端情况才需要 new 一个长度 +1 的数组。
     * 3. 另一种等价写法（先判 9 再进位，避免 %）：
     *    for (int i = digits.length - 1; i >= 0; i--) {
     *        if (digits[i] < 9) { digits[i]++; return digits; }  // 不是 9，加完就回
     *        digits[i] = 0;                                      // 是 9 → 置 0 进位
     *    }
     *    int[] res = new int[digits.length + 1]; res[0] = 1; return res;
     *    两种写法学一种吃透即可，第二种"先判断再动手"的直觉在更多模拟题里更通用。
     */

    public static void main(String[] args) {
        // 用例①：[1,2,3] → [1,2,4]
        int[] r1 = plusOne(new int[]{1, 2, 3});
        System.out.println("用例① " + java.util.Arrays.toString(r1) + "  期望 [1, 2, 4]");

        // 用例②：[4,3,2,1] → [4,3,2,2]
        int[] r2 = plusOne(new int[]{4, 3, 2, 1});
        System.out.println("用例② " + java.util.Arrays.toString(r2) + "  期望 [4, 3, 2, 2]");

        // 用例③（全 9 边界）：[9,9,9] → [1,0,0,0]（数组变长）
        int[] r3 = plusOne(new int[]{9, 9, 9});
        System.out.println("用例③ " + java.util.Arrays.toString(r3) + "  期望 [1, 0, 0, 0]");

        // 用例④（单元素边界）：[9] → [1,0]
        int[] r4 = plusOne(new int[]{9});
        System.out.println("用例④ " + java.util.Arrays.toString(r4) + "  期望 [1, 0]");
    }
}
