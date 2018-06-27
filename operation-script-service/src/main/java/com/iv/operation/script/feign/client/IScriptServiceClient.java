package com.iv.operation.script.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.common.response.ResponseDto;
import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.TemporaryScriptDto;
import com.iv.script.api.service.IScriptService;

@FeignClient(value = "script-service", fallback = ScriptServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IScriptServiceClient extends IScriptService {

}

/*
 * @Configuration class MultipartSupportConfig {
 * 
 * @Bean public Encoder feignFormEncoder() { return new SpringFormEncoder(); } }
 */

@Component
class ScriptServiceClientFallBack implements IScriptServiceClient {

	@Override
	public ScriptDto scriptInfoById(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporaryScriptDto temporaryScriptInfoById(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<byte[]> tempRead(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<byte[]> officialRead(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int tempWrite(String fileName, String type, byte[] content, Integer scriptId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResponseDto deleteTemporaryScript(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

}