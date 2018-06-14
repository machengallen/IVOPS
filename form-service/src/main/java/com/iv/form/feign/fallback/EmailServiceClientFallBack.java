package com.iv.form.feign.fallback;

import com.iv.common.response.ResponseDto;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.FormInfoTemplate;
import com.iv.form.feign.clients.EmailServiceClient;
import org.springframework.stereotype.Component;

/**
 * @author liangk
 * @create 2018年 05月 23日
 **/
@Component
public class EmailServiceClientFallBack implements EmailServiceClient {
    @Override
    public ResponseDto emailVCode(String email) {
        return null;
    }

    @Override
    public ResponseDto alarmToMail(AlarmInfoTemplate alarmInfoTemplate) {
        return null;
    }

    @Override
    public ResponseDto formToMail(FormInfoTemplate formInfoTemplate) {
        return null;
    }
}
