package com.cintsoft.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cintsoft.common.page.EntityPageBean;
import com.cintsoft.log.model.SysOperationErrorLog;
import com.cintsoft.log.vo.SysOperationErrorLogQueryVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author proven
 * @since 2021-06-24
 */
public interface SysOperationErrorLogService extends IService<SysOperationErrorLog> {

    /**
     * @param sysOperationErrorLogQueryVO 删除条件
     * @description 删除操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:52
     */
    Boolean deleteSysOperationErrorLog(SysOperationErrorLogQueryVO sysOperationErrorLogQueryVO);

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:26
     */
    Page<SysOperationErrorLog> pageSysOperationErrorLog(EntityPageBean<SysOperationErrorLogQueryVO> entityPageBean);
}
