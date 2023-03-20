package com.wingice.log.utils.transfer;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wingice.common.page.EntityPageBean;
import com.wingice.common.web.ResultBean;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 胡昊
 * Description:
 * Date: 2019/7/2
 * Time: 19:04
 * Create: DoubleH
 */
public class TransferUtils {

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

    public static <T> Page<T> fakePaginate(Integer pageNum, Integer pageSize, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new Page<>(pageNum, pageSize);
        }
        Page<T> tPage = new Page<>(pageNum, pageSize);
        if (pageSize.equals(-1)) {
            tPage.setRecords(list);
            return tPage;
        }
        int totalCount = list.size();
        int pageCount;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        tPage.setTotal(totalCount);
        tPage.setPages(pageCount);
        List<T> tList;
        if (m == 0) {
            tList = list.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
        } else {
            if (pageNum == pageCount) {
                tList = list.subList((pageNum - 1) * pageSize, totalCount);
            } else {
                tList = list.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
            }
        }
        tPage.setRecords(tList);
        return tPage;
    }

    public static <T> T getFeginResultData(ResultBean<T> resultBean) {
        return resultBean.getData();
    }

    public static List<OrderItem> transferOrderItem(String[] ascs, String[] descs) {
        final List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.addAll(OrderItem.ascs(ascs));
        orderItemList.addAll(OrderItem.descs(descs));
        return orderItemList;
    }

    public static List<OrderItem> transferOrderItem(List<String> ascs, List<String> descs) {
        final List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.addAll(OrderItem.ascs(ascs.toArray(new String[0])));
        orderItemList.addAll(OrderItem.descs(descs.toArray(new String[0])));
        return orderItemList;
    }

    public static <T> Page<T> pageEntityToPage(EntityPageBean<T> entityPageBean) {
        Page<T> page = new Page<T>(entityPageBean.getPageObject().getPageNum(), entityPageBean.getPageObject().getPageSize());
        if (entityPageBean.getPageObject().getDescs() != null && entityPageBean.getPageObject().getDescs().size() > 0) {
            page.addOrder(OrderItem.descs(entityPageBean.getPageObject().getDescs().toArray(new String[0])));
        }
        if (entityPageBean.getPageObject().getAscs() != null && entityPageBean.getPageObject().getAscs().size() > 0) {
            page.addOrder(OrderItem.ascs(entityPageBean.getPageObject().getAscs().toArray(new String[0])));
        }
        return page;
    }

    public static <T> List<OrderItem> transferOrderItem(EntityPageBean<T> entityPageBean) {
        final List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.addAll(OrderItem.ascs(entityPageBean.getPageObject().getAscs().toArray(new String[0])));
        orderItemList.addAll(OrderItem.descs(entityPageBean.getPageObject().getDescs().toArray(new String[0])));
        return orderItemList;
    }

    public static <T> List<T> treeBuild(List<T> itemList, String idField, String parentIdField, String childrenField, String topValue) {
        final List<T> topItemList = itemList.stream().filter(item -> {
            try {
                final Field field = item.getClass().getDeclaredField(parentIdField);
                field.setAccessible(true);
                return topValue.equals(String.valueOf(field.get(item)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                try {
                    final Field field = item.getClass().getSuperclass().getDeclaredField(parentIdField);
                    field.setAccessible(true);
                    return topValue.equals(String.valueOf(field.get(item)));
                } catch (NoSuchFieldException | IllegalAccessException e1) {
                    throw new RuntimeException("反射获取顶级节点失败");
                }
            }
        }).collect(Collectors.toList());
        topItemList.forEach(item -> getChildren(item, itemList, idField, parentIdField, childrenField));
        return topItemList;
    }

    private static <T> void getChildren(T node, List<T> itemList, String idField, String parentIdField, String childrenField) {
        try {
            Field field;
            try {
                field = node.getClass().getDeclaredField(idField);
            } catch (NoSuchFieldException e) {
                try {
                    field = node.getClass().getSuperclass().getDeclaredField(idField);
                } catch (NoSuchFieldException e1) {
                    throw new RuntimeException("反射节点id失败");
                }
            }
            field.setAccessible(true);
            final String id = String.valueOf(field.get(node));
            final List<T> childrenItemList = itemList.stream().filter(item -> {
                try {
                    final Field parentId = item.getClass().getDeclaredField(parentIdField);
                    parentId.setAccessible(true);
                    return id.equals(String.valueOf(parentId.get(item)));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    try {
                        final Field parentId = item.getClass().getSuperclass().getDeclaredField(parentIdField);
                        parentId.setAccessible(true);
                        return id.equals(String.valueOf(parentId.get(item)));
                    } catch (NoSuchFieldException | IllegalAccessException e1) {
                        throw new RuntimeException("反射父节点id失败");
                    }
                }
            }).collect(Collectors.toList());
            final Field childrenList = node.getClass().getDeclaredField(childrenField);
            childrenList.setAccessible(true);
            childrenList.set(node, childrenItemList);
            if (!CollectionUtils.isEmpty(childrenItemList)) {
                childrenItemList.forEach(item -> getChildren(item, itemList, idField, parentIdField, childrenField));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("反射节点id失败");
        }
    }
}
