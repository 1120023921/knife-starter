package com.cintsoft.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cintsoft.common.page.EntityPageBean;
import com.cintsoft.log.model.SysOperationLog;
import com.cintsoft.log.vo.SysOperationLogQueryVO;

/**
 * @author 胡昊
 * @description
 * @email huhao9277@gmail.com
 * @date 2022/1/19 10:28
 */
public interface SysOperationLogService extends IService<SysOperationLog> {

    /**
     * @param sysOperationLogQueryVO 删除条件
     * @description 删除操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:52
     */
    Boolean deleteSysOperationLog(SysOperationLogQueryVO sysOperationLogQueryVO);

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:26
     */
    Page<SysOperationLog> pageSysOperationLog(EntityPageBean<SysOperationLogQueryVO> entityPageBean);
}
