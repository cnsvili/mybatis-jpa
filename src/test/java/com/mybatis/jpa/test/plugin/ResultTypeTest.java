package com.mybatis.jpa.test.plugin;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.mybatis.jpa.mapper.UserMapper;
import com.mybatis.jpa.model.User;
import com.mybatis.jpa.test.AbstractTest;

public class ResultTypeTest extends AbstractTest {

	@Resource
	private UserMapper userMapper;

	@Test
	public void test() {
		long id = 118299928123543552L;

		User user = userMapper.selectById(id);
		System.out.println(JSON.toJSONString(user));

		// select twice,watch the resultMap reload times.
		User user2 = userMapper.selectById(id);
		System.out.println(JSON.toJSONString(user2));
	}

	// @Test
	public void selectUnion() {
		long id = 118299928123543552L;
		User user = userMapper.selectUnionById(id);
		System.out.println(JSON.toJSONString(user));

	}

}
