package com.cy.pj.dao;

import com.cy.pj.common.interceptor.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}