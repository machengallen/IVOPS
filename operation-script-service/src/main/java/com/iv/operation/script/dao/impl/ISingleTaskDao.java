package com.iv.operation.script.dao.impl;

import java.util.List;

import com.iv.operation.script.dto.SingleTaskPageDto;
import com.iv.operation.script.dto.SingleTaskQueryDto;
import com.iv.operation.script.entity.SingleTaskEntity;

public interface ISingleTaskDao {

	void save(SingleTaskEntity entity) throws RuntimeException;
	
	SingleTaskEntity selectById(int id) throws RuntimeException;
	
	SingleTaskPageDto selectByCondition(SingleTaskQueryDto query) throws RuntimeException;
}
