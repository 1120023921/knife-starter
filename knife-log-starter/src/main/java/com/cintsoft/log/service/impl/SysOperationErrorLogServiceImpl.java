package com.cintsoft.log.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cintsoft.common.page.EntityPageBean;
import com.cintsoft.log.mapper.SysOperationErrorLogMapper;
import com.cintsoft.log.model.SysOperationErrorLog;
import com.cintsoft.log.service.SysOperationErrorLogService;
import com.cintsoft.log.utils.transfer.TransferUtils;
import com.cintsoft.log.vo.SysOperationErrorLogQueryVO;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author proven
 * @since 2021-06-24
 */
public class SysOperationErrorLogServiceImpl extends ServiceImpl<SysOperationErrorLogMapper, SysOperationErrorLog> implements SysOperationErrorLogService {

    @Override
    public Boolean deleteSysOperationErrorLog(SysOperationErrorLogQueryVO sysOperationErrorLogQueryVO) {
        return remove(Wrappers.<SysOperationErrorLog>lambdaQuery()
                .eq(StringUtils.hasText(sysOperationErrorLogQueryVO.getId()), SysOperationErrorLog::getId, sysOperationErrorLogQueryVO.getId())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationModule()), SysOperationErrorLog::getOperationModule, sysOperationErrorLogQueryVO.getOperationModule())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationType()), SysOperationErrorLog::getOperationType, sysOperationErrorLogQueryVO.getOperationType())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationDesc()), SysOperationErrorLog::getOperationDesc, sysOperationErrorLogQueryVO.getOperationDesc())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationRequestParam()), SysOperationErrorLog::getOperationRequestParam, sysOperationErrorLogQueryVO.getOperationRequestParam())
                .eq(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationUserId()), SysOperationErrorLog::getOperationUserId, sysOperationErrorLogQueryVO.getOperationUserId())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationUserName()), SysOperationErrorLog::getOperationUserName, sysOperationErrorLogQueryVO.getOperationUserName())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationMethod()), SysOperationErrorLog::getOperationMethod, sysOperationErrorLogQueryVO.getOperationMethod())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationUrl()), SysOperationErrorLog::getOperationUrl, sysOperationErrorLogQueryVO.getOperationUrl())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationIp()), SysOperationErrorLog::getOperationIp, sysOperationErrorLogQueryVO.getOperationIp())
                .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationVersion()), SysOperationErrorLog::getOperationVersion, sysOperationErrorLogQueryVO.getOperationVersion())
                .ge(sysOperationErrorLogQueryVO.getStartTime() != null, SysOperationErrorLog::getOperationCreateTime, sysOperationErrorLogQueryVO.getStartTime())
                .le(sysOperationErrorLogQueryVO.getEndTime() != null, SysOperationErrorLog::getOperationCreateTime, sysOperationErrorLogQueryVO.getEndTime()));
    }

    @Override
    public Page<SysOperationErrorLog> pageSysOperationErrorLog(EntityPageBean<SysOperationErrorLogQueryVO> entityPageBean) {
        final SysOperationErrorLogQueryVO sysOperationErrorLogQueryVO = entityPageBean.getEntity();
        return page(new Page<SysOperationErrorLog>(entityPageBean.getPageObject().getPageNum(), entityPageBean.getPageObject().getSize())
                        .addOrder(TransferUtils.transferOrderItem(entityPageBean))
                , Wrappers.<SysOperationErrorLog>lambdaQuery()
                        .eq(StringUtils.hasText(sysOperationErrorLogQueryVO.getId()), SysOperationErrorLog::getId, sysOperationErrorLogQueryVO.getId())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationModule()), SysOperationErrorLog::getOperationModule, sysOperationErrorLogQueryVO.getOperationModule())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationType()), SysOperationErrorLog::getOperationType, sysOperationErrorLogQueryVO.getOperationType())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationDesc()), SysOperationErrorLog::getOperationDesc, sysOperationErrorLogQueryVO.getOperationDesc())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationRequestParam()), SysOperationErrorLog::getOperationRequestParam, sysOperationErrorLogQueryVO.getOperationRequestParam())
                        .eq(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationUserId()), SysOperationErrorLog::getOperationUserId, sysOperationErrorLogQueryVO.getOperationUserId())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationUserName()), SysOperationErrorLog::getOperationUserName, sysOperationErrorLogQueryVO.getOperationUserName())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationMethod()), SysOperationErrorLog::getOperationMethod, sysOperationErrorLogQueryVO.getOperationMethod())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationUrl()), SysOperationErrorLog::getOperationUrl, sysOperationErrorLogQueryVO.getOperationUrl())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationIp()), SysOperationErrorLog::getOperationIp, sysOperationErrorLogQueryVO.getOperationIp())
                        .like(StringUtils.hasText(sysOperationErrorLogQueryVO.getOperationVersion()), SysOperationErrorLog::getOperationVersion, sysOperationErrorLogQueryVO.getOperationVersion())
                        .ge(sysOperationErrorLogQueryVO.getStartTime() != null, SysOperationErrorLog::getOperationCreateTime, sysOperationErrorLogQueryVO.getStartTime())
                        .le(sysOperationErrorLogQueryVO.getEndTime() != null, SysOperationErrorLog::getOperationCreateTime, sysOperationErrorLogQueryVO.getEndTime())
        );
    }
}
