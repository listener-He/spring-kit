package org.hehh.security;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-06-22 10:00
 * @description: 集合工具
 */
public class CollectionUtil  {


    /**
     * 二分查找预插入索引实现。
     * @param target 查找元素
     */
    public   static int binSearchIndex(final List<Integer> data, Integer target) {
        if(data.isEmpty()){
            return 0;
        }
        int start = 0;
        int end = data.size() - 1;
        while (true) {
            int mid = (start + end) / 2;
            Integer key = data.get(mid);

            /**
             *   如果查询元素已存在返回-1
             */
            if (key != null && key.equals(target)) {
                return -1;
            } else if (target < key) {
                if (mid == 0 || data.get(mid - 1) < target) {
                    return mid;
                }
                end = end - 1;
            } else if (target > key) {
                if (mid == data.size() -1 || data.get(mid + 1) > target) {
                    return mid + 1;
                }
                start = mid + 1;
            }
        }
    }
}
