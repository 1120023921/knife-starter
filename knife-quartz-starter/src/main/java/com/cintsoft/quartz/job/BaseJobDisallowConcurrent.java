package com.cintsoft.quartz.job;

import org.quartz.DisallowConcurrentExecution;

/**
 * 不并发执行的任务基类，继承自 BaseJob
 *
 * @author leigq
 */
@DisallowConcurrentExecution
public class BaseJobDisallowConcurrent extends BaseJob {

}
