package com.cy.pj.common.interceptor;

import com.cy.pj.sys.utils.Tools;
import com.cy.pj.thread.threadlocal.CustomerThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @Description:    Saas客户拦截 域名通配符：*.psqweb.com
* @Author:         psq
* @CreateDate:     2021/2/22 11:04
* @Version:        1.0
*/
public class CustomerInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(CustomerInterceptor.class);
    private static final String NO_CUSTOMER = "/static/html/noCustomer.html";

    private static final String END_STR = ".psqweb.com";

    /**
     * 进入Handler方法之后，返回ModelAndView之前执行
     * 使用场景从ModelAndView参数出发，比如，将公用的模型数据在这里传入到视图，也可以统一指定显示的视图等
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 清除客户副本存储信息
        CustomerThreadLocal.removeThreadLocal();
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 在进入Handler方法之前执行了
     * <p>
     * 使用于身份认证，身份授权，登陆校验等，比如身份认证，用户没有登陆，
     * 拦截不再向下执行，返回值为 false ，即可实现拦截；否则，返回true时，拦截不进行执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        return checkCustomer(request,response);
    }

    /**
     * 检查客户
     * @return
     */
    private boolean checkCustomer(HttpServletRequest request,HttpServletResponse response){
        try {
            String customerNo = CustomerApi.getIndependentCustomerNo();
//            String customerNo = "blog_psq";
            if (StringUtils.isNotBlank(customerNo)){
                // 判断系统为 独立客户部署
                CustomerApi.setCustomerNo(customerNo);
            }else {
                // 请求URL 域名
                String serverName = request.getServerName();
                customerNo = Tools.cutStr(serverName, END_STR);
                customerNo = "blog_" + customerNo;
                // 校验客户
                boolean b = CustomerApi.getInstance().checkCustomerNo(customerNo);
                if (!b){
                    // 无此客户
                    response.sendRedirect(NO_CUSTOMER);
                    return false;
                }
                //设置客户标识
                CustomerApi.setCustomerNo(customerNo);
            }
        } catch (Exception e) {
            logger.error("Interceptor check customer error",e);
            try {
                response.sendRedirect(StringUtils.join(NO_CUSTOMER+"?errorMes="+e.getMessage()));
            } catch (IOException ex) {}
            return false;
        }
        return true;
    }
}
