package Algorithm;

/**
 * 🐳 LeetCode 34 · 在排序数组中查找元素的第一个和最后一个位置（热题100 在列）· 两次二分（翻转点）
 * 日期：2026-09-11 · 复习期第 1 周 · 力扣 AC（88/88 个测试用例通过）
 * 提交：TheLiberatianOfL · 2026.09.11 09:19
 * 说明：方法体 = 主人 AC 提交原版（仅整理缩进与格式），未改逻辑。
 *
 * 题目：给按非递减顺序排列的整数数组 nums 和 target，返回 target 的起始与结束位置；不存在返回 [-1,-1]。
 * ⚠️ 要求 O(log n) —— 必须二分，不许线性扫。
 *
 * 思路（翻转点视角 · 本题核心）：
 *   二分不是"找一个值"，而是"找单调布尔序列 F...F T...T 的翻转点"。
 *   本题左右边界是两个不同的判定函数，各跑一次二分：
 *     lowerBound: P(i) = nums[i] >= target → 第一个 true 的位置 = 左边界
 *     upperBound: P(i) = nums[i] >  target → 第一个 true 的位置 = 右边界 + 1
 *   骨架固定，只换判定符：
 *     while (left < right) {          // 区间 [left, right)
 *         mid = left + (right - left) / 2;
 *         if (P(mid)) right = mid;    // true → mid 可能是答案，不能扔
 *         else        left = mid + 1; // false → 答案一定在右边
 *     }
 *   结束时 left == right == 翻转点（收敛是设计如此，不是 bug）。
 *
 * 两版踩坑实录（2026-09-10 → 09-11，全部经实测定位）：
 *   ① 收缩漏 ±1：left = mid / right = mid → right = left + 1 时区间原地打转 → 死循环。
 *   ② 扩展段循环变量写错：while (nums[right + 1] == target) left++; 条件看 right、改的却是 left。
 *   ③ 循环条件用"值"不用"下标"（while (nums[left] < nums[right])），空数组直接越界。
 *   ④ 【思路层】"先找一个 target 再向两侧线性蹭"最坏 O(n)，不合 O(log n) → 正解是两次二分。
 *   ⑤ 【定案前最后一道坎】两个循环共用 left，第一个循环求出的左边界被第二个循环冲掉
 *      → 症状：返回的左右两数永远相同（实测 [5,5] / [1,1] / [4,4]）。修法：int lo = left; 先存住。
 *   ⑥ 上界当右边界：upperBound 是"第一个 > target"，是右边界的后一格 → 要 -1。
 *   ⑦ 区间体制混用：骨架是左闭右开（right = mid 不排除 mid），初值就必须是 nums.length；
 *      给成 nums.length - 1 会漏掉"翻转点在数组外"的位置（target 比所有数都大时左边界少一格）。
 *   ⑧ 缺"不存在"收尾：target 不存在时 lowerBound == upperBound，上界减 1 后反而比下界还小，
 *      必须靠 if (left == nums.length || nums[left] != target) 拦住 —— 这一条同时救活空数组、
 *      单元素未命中、目标过大等所有边界。
 */
public class LC34_SearchRange {

    /** AC 提交原版（常规写法：两次独立二分，各求一个翻转点） */
    public static int[] searchRange(int[] nums, int target) {
        int left = 0, right = nums.length;        // 区间 [left, right) 左闭右开
        int[] ans = new int[2];

        // 第一次二分：lowerBound —— 第一个 >= target 的位置
        while (left < right) {
            int mid = (left + right) / 2;         // 防溢出写法：left + (right - left) / 2
            if (nums[mid] >= target) {
                right = mid;                      // 可能是答案，不能扔
            } else {
                left = mid + 1;                   // 太小，答案一定在右边
            }
        }
        int lo = left;                            // ⚠️ 先存住左边界，否则会被下一个循环冲掉

        right = nums.length;
        if (left == nums.length || nums[left] != target) {   // 翻转点跑到数组外 / 对不上号 → 不存在
            ans[0] = -1;
            ans[1] = -1;
            return ans;
        }

        // 第二次二分：upperBound —— 第一个 > target 的位置（= 右边界 + 1）
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > target) {             // 与 lowerBound 只差这一个等号
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        ans[0] = lo;
        ans[1] = right - 1;                       // 上界的前一位才是右边界
        return ans;
    }

    /*
     * 🚀 优化思路 / 其他解法对照
     * ----------------------------------------
     * 复杂度：时间 O(log n)（两次二分，2·log n 仍是 O(log n)）/ 空间 O(1)。
     * → 这已是本题理论最优（要确定两个边界，至少各二分一次），没有更快的渐进解。
     *   下面三条是"换个写法"与"踩过的坑"，不是更快的算法。
     *
     * ① 暴力版（对照用，O(n)，不合本题要求，仅供理解题意）
     *    int lo = -1, hi = -1;
     *    for (int i = 0; i < nums.length; i++) {
     *        if (nums[i] == target) { if (lo == -1) lo = i; hi = i; }
     *    }
     *    return (lo == -1) ? new int[]{-1, -1} : new int[]{lo, hi};
     *    正确但 O(n) —— 题目要 O(log n)，所以必须换"两次二分"。
     *
     * ② 别用 Arrays.binarySearch 偷懒
     *    int p = Arrays.binarySearch(nums, target);   // 只保证"命中任意一个"，不保证是边界
     *    拿到 p 之后还是得往两边找边界 —— 又回到"线性蹭"的老路（最坏 O(n)）。
     *    这是本题最容易掉的坑：库函数给你"一个答案"，题目要的是"两个边界"。
     *
     * ③ 两段二分可以合并成一个 helper（消除重复代码）
     *    private static int bound(int[] nums, int target, boolean lower) {
     *        int left = 0, right = nums.length;
     *        while (left < right) {
     *            int mid = left + (right - left) / 2;
     *            if (nums[mid] > target || (lower && nums[mid] == target)) right = mid;
     *            else left = mid + 1;
     *        }
     *        return left;
     *    }
     *    调用：lowerBound → bound(nums, target, true)；upperBound → bound(nums, target, false)。
     *    💡 面试可写这版（少一段重复）；初学阶段先写两段独立二分，更容易看清"只差一个等号"。
     *
     * ④ 同一副骨架可迁移的题（都只换判定函数 P）
     *    LC704 二分查找        P: nums[i] >= target   （= lowerBound 本尊）
     *    LC35  搜索插入位置    P: nums[i] >= target   （与左边界完全同构）
     *    LC162 寻找峰值        P: nums[i] < nums[i+1] （看"趋势"不看值，翻转点 = 峰）
     *    LC33  搜索旋转排序数组 P: nums[i] >= nums[0]  （找"最小值落在第几段"的分界）
     */

    public static void main(String[] args) {
        // ① 主用例（target 存在且重复）→ [3,4]
        System.out.println("① " + java.util.Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8)) + "  期望 [3, 4]");
        // ② 坑用例：target 不存在（翻转点停在插入位置）→ [-1,-1]
        System.out.println("② " + java.util.Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6)) + "  期望 [-1, -1]");
        // ③ 边界：空数组 → [-1,-1]
        System.out.println("③ " + java.util.Arrays.toString(searchRange(new int[]{}, 0)) + "  期望 [-1, -1]");
        // ④ 边界：单元素命中 → [0,0]
        System.out.println("④ " + java.util.Arrays.toString(searchRange(new int[]{1}, 1)) + "  期望 [0, 0]");
        // ⑤ 边界：单元素未命中 → [-1,-1]
        System.out.println("⑤ " + java.util.Arrays.toString(searchRange(new int[]{1}, 0)) + "  期望 [-1, -1]");
        // ⑥ 坑用例：全相等（退化检查，曾在 9/10 版此处越界）→ [0,3]
        System.out.println("⑥ " + java.util.Arrays.toString(searchRange(new int[]{2, 2, 2, 2}, 2)) + "  期望 [0, 3]");
        // ⑦ 坑用例：比所有元素都大（翻转点在数组外）→ [-1,-1]
        System.out.println("⑦ " + java.util.Arrays.toString(searchRange(new int[]{1, 2, 3}, 4)) + "  期望 [-1, -1]");
    }
}
