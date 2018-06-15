package com.iv.script.dao;

import com.iv.script.entity.TemporaryScriptEntity;

public interface TemporaryScriptDao {
	
	TemporaryScriptEntity selectById(int id) throws RuntimeException;
	
	TemporaryScriptEntity saveOrUpdate(TemporaryScriptEntity temporaryScriptInfo) throws RuntimeException;
	
	void delById(int id) throws RuntimeException;
}
