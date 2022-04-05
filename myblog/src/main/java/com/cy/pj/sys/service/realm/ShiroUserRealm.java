package com.cy.pj.sys.service.realm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.dao.UserDao;
import com.cy.pj.entity.User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private UserDao userDao;
	/**
	 * 设置加密算法及加密次数等.
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher hcm=new HashedCredentialsMatcher();
		hcm.setHashAlgorithmName("MD5");
		hcm.setHashIterations(1);
		super.setCredentialsMatcher(hcm);
	}
	/**负责认证信息的获取及封装
	 * @param token 包含用户名和密码*/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//1.获取用户名
		UsernamePasswordToken upToken=
		(UsernamePasswordToken)token;
		String username=upToken.getUsername();
		//2.基于用户名查询数据库
		User user = userDao.findUserByUserName(username);
		//3.判定用户是否存在
		if(user==null)
		throw new UnknownAccountException();
		//5.封装用户信息并返回
		ByteSource credentialsSalt=
		ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info=
		new SimpleAuthenticationInfo(
				user,//principal用户身份
				user.getPassword(), //hashedCredentials 已加密的密码
				credentialsSalt,
				getName());//返回给SecurityManager(本身认证和授权功能)
		return info;
	}
	/**负责授权信息的获取及封装*/
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

}







