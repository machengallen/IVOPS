package com.iv.aggregation.binding;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.iv.common.enumeration.IMQExchanges;

public interface MsgSource {

	@Output(IMQExchanges.msgCre)
	MessageChannel output();
}
