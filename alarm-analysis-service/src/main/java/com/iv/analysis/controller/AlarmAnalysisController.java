package com.iv.analysis.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.iv.analysis.api.dto.AlarmInfoDto;
import com.iv.analysis.api.service.IAlarmAnalysisService;
import com.iv.common.ErrorMsg;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.ConstantContainer;

public class AlarmAnalysisController implements IAlarmAnalysisService {

	private static final Logger logger = LoggerFactory.getLogger(AlarmAnalysisController.class);
	@Override
	public ResponseDto alarmAnalysis(AlarmInfoDto infoDto) {
		
		/*try {
			String content = infoDto.getContent();
			// 基础知识库分析
			Set<String> faultDescriptionIds = new HashSet<String>(1);
			for (FaultDescriptionEntity faultDescriptionEntity : keyMatching(content, ConstantContainer.KNOWLEDGE_DB_ID)) {
				faultDescriptionIds.add(faultDescriptionEntity.getId());
			}
			
			// 租户知识库分析
			String tenantId = getTenantId(infoDto.getTenantId());
			Set<String> localFaultDescriptions = new HashSet<String>(1);
			if(null != tenantId) {
				for (FaultDescriptionEntity faultDescriptionEntity : keyMatching(content, tenantId)) {
					localFaultDescriptions.add(faultDescriptionEntity.getId());
				}
			}
			
			AlarmWithKnowledge alarmWithKnowledge = new AlarmWithKnowledge();
			alarmWithKnowledge.setId(infoDto.getId());
			alarmWithKnowledge.setFaultDescriptionIds(faultDescriptionIds);
			alarmWithKnowledge.setLocalFaultDescriptionIds(localFaultDescriptions);
			alarmWithKnowledgeDao.save(alarmWithKnowledge, infoDto.getTenantId());
			
			return ResponseDto.builder(ErrorMsg.OK);

		} catch (Exception e) {
			logger.error("告警分析失败", e);
			return ResponseDto.builder(ErrorMsg.ANALYSIS_FAILED);
		}*/
		return null;
		
	}
	
	/*private Set<FaultDescriptionEntity> keyMatching(String content, String tenantId) {
			
			List<String> keyWords = keyWordDaoImpl.selectAllNames(tenantId);
			List<String> keyMatch = new ArrayList<String>();
			Set<FaultDescriptionEntity> faultDescriptionEntities = new HashSet<FaultDescriptionEntity>(1);
			// 匹配检索
			for (String keyWord : keyWords) {
				if (content.contains(keyWord)) {
					keyMatch.add(keyWord);
				}
			}
			if(CollectionUtils.isEmpty(keyMatch)) {
				return faultDescriptionEntities;
			}
			List<KeyWordEntity> keyWordEntities = keyWordDaoImpl.selectByNames(keyMatch, tenantId);
			keyWordEntities.forEach(n -> faultDescriptionEntities.addAll(n.getFaultDescriptions()));
			
			return faultDescriptionEntities;
			
	}*/
	
	/**
	 * 获取租户id（如果是项目组id）
	 * @param id
	 */
	private String getTenantId(String id) {
		
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		
		if(id.startsWith("t")) {
			return id;
		}else if(id.startsWith("s")){
			//TODO 调用租户管理服务进行id查询
		}
		return null;
	}

}
