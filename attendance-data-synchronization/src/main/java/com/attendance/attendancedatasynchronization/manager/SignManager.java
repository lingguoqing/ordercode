package com.attendance.attendancedatasynchronization.manager;

import com.attendance.attendancedatasynchronization.common.SyncData;
import com.attendance.attendancedatasynchronization.utils.SignUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SignManager {

    @Resource
    private SyncData syncData;

    public void getSign() {
        String key = syncData.getKey();
        String secret = syncData.getSecret();
        String sign = SignUtil.getSign("/v2.0/department/init", key, secret);
        System.out.println(sign);
    }
}
