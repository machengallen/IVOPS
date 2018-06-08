package com.iv.operation.script.feign.client;

import java.io.InputStream;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.TemporaryScriptDto;
import com.iv.script.api.service.IScriptService;

@FeignClient(value = "script-service", fallback = ScriptServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IScriptServiceClient extends IScriptService{

}

@Component
class ScriptServiceClientFallBack implements IScriptServiceClient {

	@Override
	public InputStream tempRead(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream officialRead(int scriptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int tempWrite(String fileName, String type, InputStream inputStream) {
		// TODO Auto-generated method stub
		return 0;
	}

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


}