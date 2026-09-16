package Algorithm;

/**
 * 🐳 LeetCode 35 · 搜索插入位置（热题100 在列）
 * 日期：2026-09-11 建题单 · **2026-09-16 完成**
 * 状态：✅ **已 AC** —— LeetCode 提交 **66/66 通过 · 执行用时 0ms（击败 100.00%）· 内存 43.71MB（击败 98.39%）**（2026-09-16 22:02 提交）
 *
 * ── 题目 ─────────────────────────────────────────────
 * 给一个排序数组 nums（无重复元素）和一个目标值 target：
 *   找到 target → 返回它的下标；
 *   找不到     → 返回它「按顺序应该插入的位置」。
 * ⚠️ 要求 O(log n)。
 *
 * ── 核心思路（本题的全部钥匙）────────────────────────
 *  对每个位置 i，问**同一个是/否问题**：**「nums[i] 是不是 ≥ target？」**
 *  一旦定下这个判定函数 P，数组就变成 `[否 否 … 是 是]` 的形状 → **找第一个"是"** → 标准二分。
 *
 *  ⚠️ 与普通二分的唯一区别：**答案可能落在 n（数组末尾之后那个位置）**
 *     —— 例：`[1,3,5,6], 7 → 4`（全是"否"，第一个"是"在 n 上）。
 *     所以右边界要么开在 n，要么循环后补一格（下面两种写法各是一种）。
 *
 * ── 自测期望（8 组）──────────────────────────────────
 *  ① [1,3,5,6], 5 → 2      （命中，正好在中间）
 *  ② [1,3,5,6], 2 → 1      （不命中，应插在 1 和 3 之间）
 *  ③ [1,3,5,6], 7 → 4      （比所有元素都大 → 插到末尾，注意不是 3）
 *  ④ [1,3,5,6], 0 → 0      （比所有元素都小 → 插到最前）
 *  ⑤ [], 0        → 0      （空数组，不能崩）
 *  ⑥ [1], 0       → 0
 *  ⑦ [1], 2       → 1
 *  ⑧ [1], 1       → 0
 */

public class LC35_SearchInsert {

    /**
     * ✅ 主人 AC 版（LeetCode 原样提交版本 + 一行空数组保护）
     *
     * 写法特点：**左闭右闭区间 `[left, right]`，右边界开在 `n-1`**，
     *           因此循环结束后**需要补一格**（`if (nums[left] < target) left++;`）才能覆盖"答案在 n"的情况。
     *
     * 三分支结构：
     *   - `nums[mid] > target` → 答案在左半边（**mid 仍可能是答案，所以右边界收成 mid 而不是 mid-1**）
     *   - `nums[mid] < target` → 答案在右半边（mid 不可能是答案，收成 mid+1）
     *   - 相等                 → 直接命中返回
     * 循环结束时 `left == right`，该位置就是"第一个 ≥ target"的下标；**唯一例外是全部元素都 < target** → 补一格。
     */
    public static int searchInsert(int[] nums, int target) {
        if (nums.length == 0) {
            return 0;                        // ⚠️ 题单判据 ⑤：LC 官方约束是 1 <= nums.length，但**我们自己的用例有空数组**
        }
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > target) {
                right = mid;                 // mid 可能是答案 → 不收掉
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                return mid;                  // 命中
            }
        }
        if (nums[left] < target) {
            left++;                          // 全部元素都 < target → 答案落在 n
        }
        return left;
    }

    /**
     * 🔁 更优写法（同一骨架，**不需要任何补丁**）
     *
     * 改动只有两处：
     *   ① 右边界 **开在 n**（`right = nums.length`）→ 天然覆盖"答案在 n"
     *   ② 判定**合并成一个**（`>=`）→ 连"命中"都不用单独判，因为命中就是"第一个 ≥ target"
     *
     * 这正是 LC34 里"**找左边界**"的那套二分 —— 同骨架、只换判定条件。
     */
    public static int searchInsertBetter(int[] nums, int target) {
        int left = 0;
        int right = nums.length;             // ★ 开在 n：答案可能是"数组末尾之后"
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] >= target) {
                right = mid;                 // 第一个 ≥ target 的位置在 [left, mid]
            } else {
                left = mid + 1;
            }
        }
        return left;                         // 循环结束 left == right，就是答案
    }

    public static void main(String[] args) {
        int[][] cases = {
                {1, 3, 5, 6}, {1, 3, 5, 6}, {1, 3, 5, 6}, {1, 3, 5, 6},
                {}, {1}, {1}, {1}
        };
        int[] targets = {5, 2, 7, 0, 0, 0, 2, 1};
        int[] expect = {2, 1, 4, 0, 0, 0, 1, 0};

        System.out.println("=== 判据 8 组：AC 版 vs 更优版 ===");
        boolean allPass = true;
        for (int i = 0; i < cases.length; i++) {
            int a = searchInsert(cases[i], targets[i]);
            int b = searchInsertBetter(cases[i], targets[i]);
            boolean ok = (a == expect[i]) && (b == expect[i]);
            allPass = allPass && ok;
            System.out.printf("第%2d 组：AC版=%d · 更优版=%d · 期望=%d  %s%n",
                    i + 1, a, b, expect[i], ok ? "✅" : "❌");
        }
        System.out.println(allPass ? ">>> 8 组全部通过 🎉" : ">>> 有未通过项");
    }
}
