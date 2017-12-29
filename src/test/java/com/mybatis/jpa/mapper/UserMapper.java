package com.mybatis.jpa.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.mybatis.jpa.model.User;

/**
 * 
 * @author svili
 *
 */
@Repository
public interface UserMapper {
	
	User selectById(long userId);
	
	@Select("SELECT t.*,a.* FROM t_sys_user t,t_sys_user_archive a WHERE t.user_id = a.user_id and t.user_id = #{userId}")
	User selectUnionById(long userId);

}
