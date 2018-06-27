package com.iv.form.feign.fallback;

import com.iv.common.response.ResponseDto;
import com.iv.dto.TemplateFormMessageDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.dto.WeChat;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.form.feign.clients.WechatServiceClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liangk
 * @create 2018年 05月 23日
 **/
@Component
public class WechatServiceClientFallBack implements WechatServiceClient {

    @Override
    public ResponseDto getWechatLoginCode() {
        return null;
    }

    @Override
    public ResponseDto wxLoginCallBack(String code, HttpServletRequest request) {
        return null;
    }

    @Override
    public String xxtInterface(WeChat wc) {
        return null;
    }

    @Override
    public ResponseDto qrcodeCreate(int userId) {
        return null;
    }

    @Override
    public String getWeiXinMessage(HttpServletRequest request) throws Exception {
        return null;
    }

    @Override
    public UserWechatEntityDto selectUserWechatByUnionid(String unionid) {
        return null;
    }

    @Override
    public ResponseDto SendWeChatInfo(TemplateMessageDto templateMessageDto) {
        return null;
    }

    @Override
    public boolean ifFocusWechat(int userId) {
        return false;
    }

    @Override
    public ResponseDto SendFormWeChatInfo(TemplateFormMessageDto templateFormMessageDto) {
        return null;
    }

    @Override
    public String getUnionid(String code) {
        return null;
    }
}
