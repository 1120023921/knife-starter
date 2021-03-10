package com.cintsoft.mybatis.plus.utils;

import java.util.List;

/**
 * @author 胡昊
 * Description: 分页工具
 * Date: 2020/2/17
 * Time: 17:04
 * Mail: huhao9277@gmail.com
 */
public class PaginationUtils {

    /**
     * @param list     待分页列表
     * @param pageNum  页号
     * @param pageSize 页大小
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2020/2/17 17:14
     */
    public static <T> List<T> paginate(List<T> list, Integer pageNum, Integer pageSize) {
        int totalCount = list.size();
        int pageCount;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        if (m == 0) {
            return list.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
        } else {
            if (pageNum == pageCount) {
                return list.subList((pageNum - 1) * pageSize, totalCount);
            } else {
                return list.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
            }
        }
    }
}
