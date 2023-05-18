package com.wingice.log;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.wingice.**.mapper")
public class PackageScanConfig {
}
