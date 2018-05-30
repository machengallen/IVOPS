package com.iv.script.dao;

import java.util.List;

import com.iv.script.api.dto.ScriptQueryDto;
import com.iv.script.entity.AuthorEntity;
import com.iv.script.entity.ScriptEntity;
import com.iv.script.entity.ScriptPagingWrap;

public interface IScriptDao {

	void save(ScriptEntity entity) throws RuntimeException;
	
	List<ScriptEntity> selectAll(int first, int max) throws RuntimeException;
	
	ScriptEntity selectById(int id) throws RuntimeException;
	
	void delById(int id) throws RuntimeException;
	
	void delByName(String name) throws RuntimeException;
	
	ScriptEntity selectByName(String name, String type) throws RuntimeException;
	
	long countAll() throws RuntimeException;
	
	ScriptPagingWrap selectByCondition(ScriptQueryDto query, List<AuthorEntity> creators) throws RuntimeException;
}
