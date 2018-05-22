package com.iv.entity.dto;

import java.util.ArrayList;
import java.util.Set;

public class UserWechatEntityDto {
	// 开放平台唯一id
		private String unionid;	
		private String openid;
		// 用户昵称
		private String nickname;
		// 电话
		private String tel;
		// 性别
		private byte sex;
		// 用户语言
		private String language;
		// 用户所在城市
		private String city;
		// 用户所在省份
		private String province;
		// 用户所在国家
		private String country;
		// 公众号对关注者的备注
		private String remark;
		// 公众号对关注者设置的分组ID
		private Integer groupid;
		// 标签id列表
		private ArrayList<Integer> tagid_list;
		// 是否关注
		private byte subscribe;
		// 用户头像url
		private String headimgurl;
		// 用户关注时间
		private Long subscribe_time;
		// 用户对于公众号的唯一标识
		private Set<PlatformSignDto> platformSigns;
		public String getUnionid() {
			return unionid;
		}
		public void setUnionid(String unionid) {
			this.unionid = unionid;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public byte getSex() {
			return sex;
		}
		public void setSex(byte sex) {
			this.sex = sex;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public Integer getGroupid() {
			return groupid;
		}
		public void setGroupid(Integer groupid) {
			this.groupid = groupid;
		}
		public ArrayList<Integer> getTagid_list() {
			return tagid_list;
		}
		public void setTagid_list(ArrayList<Integer> tagid_list) {
			this.tagid_list = tagid_list;
		}
		public byte getSubscribe() {
			return subscribe;
		}
		public void setSubscribe(byte subscribe) {
			this.subscribe = subscribe;
		}
		public String getHeadimgurl() {
			return headimgurl;
		}
		public void setHeadimgurl(String headimgurl) {
			this.headimgurl = headimgurl;
		}
		public Long getSubscribe_time() {
			return subscribe_time;
		}
		public void setSubscribe_time(Long subscribe_time) {
			this.subscribe_time = subscribe_time;
		}
		public Set<PlatformSignDto> getPlatformSigns() {
			return platformSigns;
		}
		public void setPlatformSigns(Set<PlatformSignDto> platformSigns) {
			this.platformSigns = platformSigns;
		}		
				
		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
		public void filterEmoji() {
			setNickname(this.nickname.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
					""));
		}
		
}
