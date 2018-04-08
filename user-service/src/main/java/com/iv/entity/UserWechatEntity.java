package com.iv.entity;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户微信账号信息
 * 
 * @author macheng
 *
 */
@Entity
@Table(name = "User_Wechat_Info")
public class UserWechatEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6863210001150090725L;
	// 用户对于公众号的唯一标识
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
	// 开放平台唯一id
	private String unionid;

	public UserWechatEntity(String openid, String nickname, byte sex, String language, String city,
			String province, String country, String remark, Integer groupid, ArrayList<Integer> tagid_list,
			byte subscribe, String headimgurl, Long subscribe_time, String unionid) {
		super();
		this.openid = openid;
		this.nickname = nickname;
		this.sex = sex;
		this.language = language;
		this.city = city;
		this.province = province;
		this.country = country;
		this.remark = remark;
		this.groupid = groupid;
		this.tagid_list = tagid_list;
		this.subscribe = subscribe;
		this.headimgurl = headimgurl;
		this.subscribe_time = subscribe_time;
		this.unionid = unionid;
	}

	public UserWechatEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public void filterEmoji() {
		setNickname(this.nickname.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
				""));
	}

	@Override
	public String toString() {
		return "UserWechatEntity [openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", language="
				+ language + ", city=" + city + ", province=" + province + ", country=" + country + ", remark=" + remark
				+ ", groupid=" + groupid + ", tagid_list=" + tagid_list + ", subscribe=" + subscribe + ", headimgurl="
				+ headimgurl + ", subscribe_time=" + subscribe_time + ", unionid=" + unionid + "]";
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((groupid == null) ? 0 : groupid.hashCode());
		result = prime * result + ((headimgurl == null) ? 0 : headimgurl.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((openid == null) ? 0 : openid.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + sex;
		result = prime * result + subscribe;
		result = prime * result + ((subscribe_time == null) ? 0 : subscribe_time.hashCode());
		result = prime * result + ((tagid_list == null) ? 0 : tagid_list.hashCode());
		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
		result = prime * result + ((unionid == null) ? 0 : unionid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserWechatEntity other = (UserWechatEntity) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (groupid == null) {
			if (other.groupid != null)
				return false;
		} else if (!groupid.equals(other.groupid))
			return false;
		if (headimgurl == null) {
			if (other.headimgurl != null)
				return false;
		} else if (!headimgurl.equals(other.headimgurl))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (openid == null) {
			if (other.openid != null)
				return false;
		} else if (!openid.equals(other.openid))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (sex != other.sex)
			return false;
		if (subscribe != other.subscribe)
			return false;
		if (subscribe_time == null) {
			if (other.subscribe_time != null)
				return false;
		} else if (!subscribe_time.equals(other.subscribe_time))
			return false;
		if (tagid_list == null) {
			if (other.tagid_list != null)
				return false;
		} else if (!tagid_list.equals(other.tagid_list))
			return false;
		if (tel == null) {
			if (other.tel != null)
				return false;
		} else if (!tel.equals(other.tel))
			return false;
		if (unionid == null) {
			if (other.unionid != null)
				return false;
		} else if (!unionid.equals(other.unionid))
			return false;
		return true;
	}
	
}
