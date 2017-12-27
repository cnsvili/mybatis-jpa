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
	
	@Select("SELECT * FROM t_sys_user WHERE uni_code = #{unicode}")
	User selectByUnicode(String unicode);

}
