package com.iv.message.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import com.iv.common.enumeration.IMQExchanges;

public interface MsgSink {

	String INPUT = IMQExchanges.msgCre;
	
	@Input(MsgSink.INPUT)
	SubscribableChannel input();
}
