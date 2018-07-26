package com.iv.message.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iv.common.response.ResponseDto;
import com.iv.message.api.dto.MsgInputDto;

public interface IMessageService {

	/**
	 * 消息接收
	 * @param msgInput
	 * @return
	 */
	@RequestMapping(value = "/msg/input", method = RequestMethod.POST)
	ResponseDto msgInput(@RequestBody MsgInputDto msgInput);
	
}
