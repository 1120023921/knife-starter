package com.wingice.log.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wingice.common.page.EntityPageBean;
import com.wingice.log.mapper.SysOperationLogMapper;
import com.wingice.log.model.SysOperationLog;
import com.wingice.log.service.SysOperationLogService;
import com.wingice.log.utils.transfer.TransferUtils;
import com.wingice.log.vo.SysOperationLogQueryVO;
import org.springframework.util.StringUtils;

/**
 * @author 胡昊
 * @description
 * @email huhao9277@gmail.com
 * @date 2022/1/19 10:28
 */
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Override
    public Boolean deleteSysOperationLog(SysOperationLogQueryVO sysOperationLogQueryVO) {
        return remove(Wrappers.<SysOperationLog>lambdaQuery()
                .eq(StringUtils.hasText(sysOperationLogQueryVO.getId()), SysOperationLog::getId, sysOperationLogQueryVO.getId())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationModule()), SysOperationLog::getOperationModule, sysOperationLogQueryVO.getOperationModule())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationType()), SysOperationLog::getOperationType, sysOperationLogQueryVO.getOperationType())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationDesc()), SysOperationLog::getOperationDesc, sysOperationLogQueryVO.getOperationDesc())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationRequestParam()), SysOperationLog::getOperationRequestParam, sysOperationLogQueryVO.getOperationRequestParam())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationResponseParam()), SysOperationLog::getOperationResponseParam, sysOperationLogQueryVO.getOperationResponseParam())
                .eq(StringUtils.hasText(sysOperationLogQueryVO.getOperationUserId()), SysOperationLog::getOperationUserId, sysOperationLogQueryVO.getOperationUserId())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationUserName()), SysOperationLog::getOperationUserName, sysOperationLogQueryVO.getOperationUserName())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationMethod()), SysOperationLog::getOperationMethod, sysOperationLogQueryVO.getOperationMethod())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationUrl()), SysOperationLog::getOperationUrl, sysOperationLogQueryVO.getOperationUrl())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationIp()), SysOperationLog::getOperationIp, sysOperationLogQueryVO.getOperationIp())
                .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationVersion()), SysOperationLog::getOperationVersion, sysOperationLogQueryVO.getOperationVersion())
                .ge(sysOperationLogQueryVO.getStartTime() != null, SysOperationLog::getOperationCreateTime, sysOperationLogQueryVO.getStartTime())
                .le(sysOperationLogQueryVO.getEndTime() != null, SysOperationLog::getOperationCreateTime, sysOperationLogQueryVO.getEndTime()));
    }

    @Override
    public Page<SysOperationLog> pageSysOperationLog(EntityPageBean<SysOperationLogQueryVO> entityPageBean) {
        final SysOperationLogQueryVO sysOperationLogQueryVO = entityPageBean.getEntity();
        return page(new Page<SysOperationLog>(entityPageBean.getPageObject().getPageNum(), entityPageBean.getPageObject().getSize())
                        .addOrder(TransferUtils.transferOrderItem(entityPageBean))
                , Wrappers.<SysOperationLog>lambdaQuery()
                        .eq(StringUtils.hasText(sysOperationLogQueryVO.getId()), SysOperationLog::getId, sysOperationLogQueryVO.getId())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationModule()), SysOperationLog::getOperationModule, sysOperationLogQueryVO.getOperationModule())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationType()), SysOperationLog::getOperationType, sysOperationLogQueryVO.getOperationType())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationDesc()), SysOperationLog::getOperationDesc, sysOperationLogQueryVO.getOperationDesc())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationRequestParam()), SysOperationLog::getOperationRequestParam, sysOperationLogQueryVO.getOperationRequestParam())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationResponseParam()), SysOperationLog::getOperationResponseParam, sysOperationLogQueryVO.getOperationResponseParam())
                        .eq(StringUtils.hasText(sysOperationLogQueryVO.getOperationUserId()), SysOperationLog::getOperationUserId, sysOperationLogQueryVO.getOperationUserId())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationUserName()), SysOperationLog::getOperationUserName, sysOperationLogQueryVO.getOperationUserName())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationMethod()), SysOperationLog::getOperationMethod, sysOperationLogQueryVO.getOperationMethod())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationUrl()), SysOperationLog::getOperationUrl, sysOperationLogQueryVO.getOperationUrl())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationIp()), SysOperationLog::getOperationIp, sysOperationLogQueryVO.getOperationIp())
                        .like(StringUtils.hasText(sysOperationLogQueryVO.getOperationVersion()), SysOperationLog::getOperationVersion, sysOperationLogQueryVO.getOperationVersion())
                        .ge(sysOperationLogQueryVO.getStartTime() != null, SysOperationLog::getOperationCreateTime, sysOperationLogQueryVO.getStartTime())
                        .le(sysOperationLogQueryVO.getEndTime() != null, SysOperationLog::getOperationCreateTime, sysOperationLogQueryVO.getEndTime())
        );
    }
}
