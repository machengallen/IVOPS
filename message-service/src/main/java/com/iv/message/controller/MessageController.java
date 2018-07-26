package com.iv.message.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.dto.ObjectPageDto;
import com.iv.common.response.ResponseDto;
import com.iv.message.api.constant.ErrorMsg;
import com.iv.message.api.dto.MsgClearReq;
import com.iv.message.api.dto.MsgFrontQueryDto;
import com.iv.message.api.dto.MsgInputDto;
import com.iv.message.api.service.IMessageService;
import com.iv.message.entity.MsgEntity;
import com.iv.message.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(description = "消息中心系列接口")
public class MessageController implements IMessageService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private MessageService service;

	@ApiIgnore
	@Override
	@ApiOperation("消息接收")
	public ResponseDto msgInput(@RequestBody MsgInputDto msgInput) {

		try {
			service.msgInput(msgInput);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("消息处理失败", e);
			return ResponseDto.builder(ErrorMsg.PRODUCE_MSG_FAILED);
		}
	}
	
	@PostMapping("/get/msg")
	public ResponseDto getMsgPaging(@RequestBody MsgFrontQueryDto queryDto) {
		try {
			ObjectPageDto<MsgEntity> data = service.getMsgPaging(queryDto);
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(data);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取消息列表失败", e);
			return ResponseDto.builder(ErrorMsg.GET_SYS_MESSAGE_FAILED);
		}
	}
	
	@PostMapping("/mark/read")
	public ResponseDto mark(MsgClearReq clearReq) {
		try {
			service.mark(clearReq.getIds());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("消息标记失败", e);
			return ResponseDto.builder(ErrorMsg.UPDATE_MSG_FAILED);
		}
	}
	
	@GetMapping("/mark/read/all")
	public ResponseDto markAll() {
		
		try {
			service.markAll();
			return ResponseDto.builder(ErrorMsg.OK);
			} catch (Exception e) {
				LOGGER.error("消息标记失败", e);
				return ResponseDto.builder(ErrorMsg.UPDATE_MSG_FAILED);
			}
	}
	
	@PostMapping("/delete")
	public ResponseDto delete(MsgClearReq clearReq) {
		
		try {
			service.delete(clearReq.getIds());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("消息标记失败", e);
			return ResponseDto.builder(ErrorMsg.UPDATE_MSG_FAILED);
		}
	}
	
	/*@GetMapping("/testMsg")
	public void testMsg(@RequestParam int userId, @RequestParam String msg, @RequestParam(required=false) MsgType type) {
		MessageWebSocket.webSocketMap.get(userId).sendMsg(msg);
	}*/
}
