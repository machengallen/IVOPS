package com.iv.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.iv.dto.UserIdsPagingDto;
import com.iv.entity.GroupEntity;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.outer.dto.GroupEntityDto;

/**
 * 组相关接口实现
 * @author zhangying
 * 2018年4月13日
 * aggregation-1.4.0-SNAPSHOT
 */
@Repository
public class IGroupDaoImpl implements IGroupDao {

	@Override
	public GroupEntity selectGroupInfoById(short id) throws RuntimeException {
		// TODO Auto-generated method stub
		return (GroupEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(GroupEntity.class, id);
			}
		});
	}

	@Override
	public GroupEntity selectGroupInfoByTenantAndId(short id, String tenantId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (GroupEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(GroupEntity.class, id);
			}
		}, tenantId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupEntity> selectGroupAll() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<GroupEntity>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from GroupEntity").list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupEntity> selectGroupsByUserId(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<GroupEntity>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from GroupEntity g join g.userIds u where u=?")
						.setParameter(0, userId).list();	
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 查询所有组的id、name
	 */
	public List<Object[]> selectGroupAllIdAndName() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select g.groupId, g.groupName from GroupEntity g").list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 查询用户所在组的id、name
	 */
	public List<Object[]> selectGroupIdAndNameByUserId(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select g.groupId, g.groupName from GroupEntity g join g.userIds u where u=?")
						.setParameter(0, userId).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 查询多个团队人员分页信息
	 * List<GroupEntity> groups = new ArrayList<GroupEntity>();
					for (Short id : ids) {
						groups.add(ses.get(GroupEntity.class, id));
					}
					return groups;
	 */
	public List<UserIdsPagingDto> groupUserPageInfo(int curItems, int item, List<GroupEntityDto> GroupEntitys) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<UserIdsPagingDto>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				List<UserIdsPagingDto> userIdsPagings = new ArrayList<UserIdsPagingDto>();
				for (GroupEntityDto GroupEntity : GroupEntitys) {
					UserIdsPagingDto userIdsPagingDto = new UserIdsPagingDto();
					userIdsPagingDto.setUserIds(ses.createQuery("select u from GroupEntity g join g.userIds u where g.groupId=?")
							.setParameter(0, GroupEntity.getGroupId()).setFirstResult(curItems).setMaxResults(item).list());
					userIdsPagingDto.setTotalCount((long)ses.createQuery("select count(1) from GroupEntity g join g.userIds where g.groupId=?")
							.setParameter(0, GroupEntity.getGroupId()).uniqueResult());
					userIdsPagingDto.setGroupId(GroupEntity.getGroupId());
					userIdsPagingDto.setGroupName(GroupEntity.getGroupName());
					userIdsPagings.add(userIdsPagingDto);
				}
				return userIdsPagings;
			}
		});
	}

	@Override
	/**
	 * 保存与更新团队
	 */
	public void saveOrUpdateGroup(GroupEntity groupEntity) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(groupEntity);
				return null;
			}
		});
	}

	@Override
	/**
	 * 根据组id 查询组实体
	 */
	public GroupEntity selectGroupById(short id) throws RuntimeException {
		// TODO Auto-generated method stub
		return (GroupEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(GroupEntity.class, id);
			}
		});
	}

	@Override
	/**
	 * 根据组Id删除组
	 */
	public void deleteGroupById(short id) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.load(GroupEntity.class, id));
				return null;
			}
		});
	}

	@Override
	/**
	 * 更改组名称
	 */
	public GroupEntity updateGroupName(short id, String groupName) throws RuntimeException {
		// TODO Auto-generated method stub
		return (GroupEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				if(!StringUtils.isEmpty(groupName)){
					GroupEntity groupEntity = (GroupEntity) ses.get(GroupEntity.class, id);
					groupEntity.setGroupName(groupName);
					return groupEntity;
				}
				return null;
			}
		});
	}

	/**
	 * 根据组Ids获取组信息（组名称、组id）
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GroupEntity> groupsInfo(List<Short> groupIds) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<GroupEntity>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				List<GroupEntity> groupEntitys = new ArrayList<GroupEntity>();
				for (Short groupId : groupIds) {
					groupEntitys.add(ses.get(GroupEntity.class, groupId));
				}
				return groupEntitys;
				/*return ses.createQuery("from GroupEntity g where g.groupId in :groupIds")
						.setParameterList("groupIds", groupIds).list();*/
			}
		});
	}

	/**
	 * 组内人员id查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> selectGroupUserIds(String subTenantId, short groupId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select u from GroupEntity g join g.userIds u where g.groupId=?")
						.setParameter(0, groupId).list();
			}
		}, subTenantId);
	}


}
