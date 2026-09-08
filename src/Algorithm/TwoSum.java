package Algorithm;

/**
 * 🐳 LeetCode 1 · 两数之和（热题100 #1）· 枚举（暴力）版
 * 日期：2026-09-09 · 复习期第 1 周 · 力扣 AC（65/65 通过）
 * 提交：TheLiberatianOfL · 07:20 · 用时 49ms / 内存 46.10MB
 *
 * 思路：枚举——两重 for 把所有"两数组合"试一遍，和 == target 就记下标。
 * 特征：题目保证唯一解；暴力版 O(n²)，为后续哈希 O(n) 版做引子。
 *
 * 易错点：题目要求"不能重复使用同一个元素"，用 j=i+1 保证两数不取同一个；
 *         经典坑：[3,2,4] target=6 答案是 [1,2]，不是 [0,0]（3+3 违反规则）。
 */
public class TwoSum {

    public static int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        for (int i = 0; i < nums.length; i++) {
            // j 从 i+1 起，避免跟 i 取同一个元素
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    ans[0] = i;
                    ans[1] = j;
                    return ans;   // 题目保证唯一解，找到即可返回，不必再扫
                }
            }
        }
        return ans;               // 理论走不到（题面保证一定有解）
    }

    /*
     * 🚀 优化思路（更优解法 · 哈希表 O(n)）
     * ----------------------------------------
     * 暴力版每一对组合都试，O(n²)。改成"边扫边查"：
     * 用一个 HashMap 记录"已经出现的数 → 下标"，遍历到 nums[i] 时，
     * 只查 target - nums[i] 这个"缺的数"之前在不在 map 里；在就直接返回。
     * 一次遍历搞定：O(n) 时间 / O(n) 空间，把"枚举所有对"换成"补数查询"。
     *
     * import java.util.HashMap;
     * import java.util.Map;
     * public int[] twoSumHash(int[] nums, int target) {
     *     Map<Integer, Integer> map = new HashMap<>();
     *     for (int i = 0; i < nums.length; i++) {
     *         int need = target - nums[i];              // 我还缺哪个数
     *         if (map.containsKey(need)) {
     *             return new int[]{map.get(need), i};   // 之前见过的下标 + 当前 i
     *         }
     *         map.put(nums[i], i);                      // 记住"这个数出现过，下标是 i"
     *     }
     *     return new int[0];
     * }
     *
     * 💡 复习提示：HashMap 是 W2 集合框架的内容，现在先看懂思路，
     *    等 W2 复健集合时再来亲手写一遍（这正是从"暴力枚举"到"空间换时间"的经典演进）。
     */

    public static void main(String[] args) {
        // 用例①：[2,7,11,15], 9 → [0,1]
        int[] r1 = twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println("用例① [" + r1[0] + ", " + r1[1] + "]  期望 [0, 1]");

        // 用例②：[3,2,4], 6 → [1,2]（不是 [0,0]，不能拿同一个元素加自己）
        int[] r2 = twoSum(new int[]{3, 2, 4}, 6);
        System.out.println("用例② [" + r2[0] + ", " + r2[1] + "]  期望 [1, 2]");

        // 用例③：[3,3], 6 → [0,1]（两个不同位置的 3）
        int[] r3 = twoSum(new int[]{3, 3}, 6);
        System.out.println("用例③ [" + r3[0] + ", " + r3[1] + "]  期望 [0, 1]");
    }
}
