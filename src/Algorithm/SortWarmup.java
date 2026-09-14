package Algorithm;

/**
 * 🐳 手写题单：三个基础排序（为手写快排热身）
 * 日期：2026-09-11 · 复习期第1周 · 周五场（19:00 档）
 *
 * 要求（每个都手写，不许调 Arrays.sort / Collections.sort）：
 *  1. bubbleSort(int[] a)    冒泡：相邻比较交换，每轮把当前最大值"冒"到末尾
 *  2. selectionSort(int[] a) 选择：每轮从未排序段选出最小值，放到已排序段末尾
 *  3. insertionSort(int[] a) 插入：把当前元素往前面的已排序段里"插"到正确位置
 *
 * 自测期望（统一用例）：
 *  {5, 2, 9, 1, 5, 6} → 每种排序后都应是 {1, 2, 5, 5, 6, 9}（三种结果必须一致）
 *  建议每种排完各打印一遍，顺带数一下各自的比较次数 / 交换次数
 *
 * 附加边界用例（别只测一个）：
 *  空数组 {} · 单元素 {7} · 已经有序 {1,2,3,4} · 全相等 {2,2,2}
 *
 * ⚠️ 核心考点：
 *   - 三者平均/最坏都是 O(n²)，最好情况：冒泡(带标志位优化)/插入 接近 O(n)，选择恒定 O(n²)
 *   - 插入排序对"接近有序"的数据最友好 —— 记住这点，后面快排的优化（小区间转插入排序）用得上
 *   - 下标边界：内层循环的起点/终点差一格就是越界，写完先跑空数组
 */

// TODO 2026-09-11：主人默写区 —— 从空类开始写，写完跑自测期望
public class SortWarmup {
    public static int[] bublleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        return arr;
    }
    public static int[] selectedSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i+1; j < arr.length ; j++) {
                if (arr[min] > arr[j]) {
                    min=j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[min];
            arr[min] = temp;

        }
        return arr;
    }
    public static void insertionSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int change = arr[i];
            int j;
            for (j = i - 1; j >=0 && arr[j] >change; j--) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = change;
        }
    }
}
