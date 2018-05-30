package com.iv.script.dao;

import java.util.List;

import com.iv.script.entity.AuthorEntity;

public interface IAuthorDao {

	void save(AuthorEntity entity) throws RuntimeException;
	
	AuthorEntity selectById(int userId) throws RuntimeException;
	
	List<AuthorEntity> selectByRealName(String name) throws RuntimeException;
}
