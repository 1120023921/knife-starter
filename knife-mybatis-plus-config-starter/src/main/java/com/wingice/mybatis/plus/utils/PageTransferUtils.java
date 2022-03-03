package com.wingice.mybatis.plus.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2019/7/2
 * Time: 19:04
 * Create: DoubleH
 */
public class PageTransferUtils {

    /**
     * 分页信息转换
     *
     * @param source  源分页信息
     * @param records 目标数据列表
     * @return 目标分页信息
     */
    @SuppressWarnings("rawtypes")
    public static <T> Page<T> transferPageInfo(Page source, List<T> records) {
        final Page<T> target = new Page<>();
        target.setRecords(records);
        target.setCurrent(source.getCurrent());
        target.setPages(source.getPages());
        target.setSize(source.getSize());
        target.setTotal(source.getTotal());
        return target;
    }

    public static <T> Page<T> fakePaginate(Integer pageNum, Integer pageSize, Page<T> page) {
        final List<T> list = page.getRecords();
        page.setRecords(PaginationUtils.paginate(list, pageNum, pageSize));
        int totalCount = list.size();
        int pageCount;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page.setPages(pageCount);
        return page;
    }

    public static List<OrderItem> transferOrderItem(String[] ascs, String[] descs) {
        final List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.addAll(OrderItem.ascs(ascs));
        orderItemList.addAll(OrderItem.descs(descs));
        return orderItemList;
    }
}
