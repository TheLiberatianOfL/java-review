package Algorithm;

/**
 * 🐳 默写题单：快速排序（脱稿手写）
 * 日期：2026-09-19 · 复习期第2周 · 救火日 · 算法线
 *
 * 要求（脱稿写，写完跑用例）：
 *  1. quickSort(int[] arr)：**原地**排序，不许 new 新数组
 *  2. partition(...)：选一个基准 pivot，把 ≤ pivot 的放左边、> pivot 的放右边，返回基准最终落点下标
 *     ⚠️ pivot 选法自己定（最左 / 最右 / 中间），并在注释里说明选它的理由
 *  3. 递归处理左右两段；递归出口 = 区间长度 < 2
 *  4. 选做：写完后再写一个不同风格的 partition（挖坑填数 vs 左右指针交换），对比哪个更好讲
 *
 * 自测期望（main 里跑出来）：
 *  ① {5,3,8,1,9,2,7} → {1,2,3,5,7,8,9}
 *  ② {}              → {} 不崩
 *  ③ {1}             → {1}
 *  ④ {2,2,2,2}       → {2,2,2,2}（大量重复元素不越界、不死循环）
 *  ⑤ {9,8,7,6,5}     → {5,6,7,8,9}（完全逆序 = 最坏情况观察点）
 *  ⑥ 与 Arrays.sort 的结果逐元素比对一致
 *
 * ⚠️ 核心考点：
 *  - 复杂度：平均 O(n log n) · 最坏 O(n²) · 额外空间 O(log n)（栈深度）—— 要能讲出「最坏出现在什么输入」
 *  - 稳定性：快排**不稳定**，要能说出原因（跨越式交换会打乱相等元素的相对顺序）
 *  - 边界：分区时不等号放哪边（放错 → 死循环 / 下标越界），这是本题最大的坑
 */

// TODO 2026-09-19：主人默写区 —— 从空类开始写，写完对照自测期望验证
public class QuickSort {
    public static int partition(int[] arr, int left, int right) {
        int pivot = arr[left];// 取最左当基准：实现最简、不需额外空间
        int i = left, j = right;
        while (i < j) {
            while (i < j && arr[j] >= pivot) {
                j--;
            }
            arr[i] = arr[j];
            while (i < j && arr[i] <= pivot) {
                i++;
            }
            arr[j] = arr[i];
        }
        arr[i]=pivot;
        return i;
    }
    public static void quickSort(int[] arr){
        if(arr==null||arr.length<2){
            return;
        }
        quickSort(arr,0,arr.length-1);
    }
    public static void quickSort(int[] arr, int left, int right) {
        if (left >=right) {
            return;
        }
        int pivot =partition(arr, left, right);
        quickSort(arr, left, pivot - 1);
        quickSort(arr, pivot + 1, right);
    }
    static void show(int[] a) {
        for (int i : a) System.out.print(i + " ");
        System.out.println();
    }
    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        int[] arr1 = {5,3,8,1,9,2,7};
        int[] arr2={};
        int[] arr3={1};
        int[] arr4={2,2,2,2};
        int[] arr5={9,8,7,6,5};
        quickSort(arr1);
        show(arr1);
        quickSort(arr2);
        show(arr2);
        quickSort(arr3);
        show(arr3);
        quickSort(arr4);
        show(arr4);
        quickSort(arr5);
        show(arr5);
    }


}
