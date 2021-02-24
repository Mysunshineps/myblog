package com.cy.pj.dao.enums;

public interface Constants {
    /**
     * 默认 sass 库 标识
     */
    String DEFAULT_CUSTOMER_NO = "blog_saas";
    String EMPTYSTR = "";
    String UNDERLINE = "_";

    interface NumberStr{
        String MINUS_ONE = "-1";
        String ZORE = "0";
        String ONE = "1";
        String TWO = "2";
    }

    interface Sync{
        String PRICE = "price";
        String STOCK = "stock";
        String ALL = "ALL";
    }

    interface SecondsTime{
        /**
         * 23 小时 秒数
         */
        int TWENTY_THREE_HOUR = 82800;
    }

    String SUCCESS_CODE = "200";

    String ERROR_CODE = "500";

    /**
     * 消息key
     */
    String MESSAGE = "message";

    /**
     * 错误key
     */
    String ERROR = "error";



    /**
     * 当前登录的用户
     */
    String CURRENT_USER = "user";

    /**
     * 当前登录用户名
     */
    String CURRENT_USERNAME = "username";

    /**
     * 状态码
     */
     int DELETE = 0;
    /**
     * 未被删除状态，默认状态
     */
    int NORMAL = 1;



    /**
     * 状态
     */
    String SUCCESS = "success";

    String FAIL = "fail";

    String CRM_SUCCESS = NumberStr.ZORE;

    /**
     * 系统参数设置项
     */
    interface SET {

        /**
         * 拉取三方商品时是否自动关联
         * 1 : 自动关联
         * 非 1 ： 不自动关联
         */
        String AUTO_RELATION = "set.auto.relation";

        /**
         * 有赞多网点模式
         * 1 ：启用多网点
         * 0 ：关闭多网点模式
         */
        String YOUZAN_MULTIPOINT = "set.youzan.multipoint";

        /**
         * 有赞不同步价格库存类别，前两位
         *
         */
        String YOUZAN_NOT_SYNC_CODE = "set.youzan.not.sync.code";

        /**
         * 已关联商品是否跟新
         * 1 ：跟新
         * 2 ：不跟新
         */
        String RELATION_GOODS_UPDATE = "set.relation.goods.update";

        /**
         *  是否自动审核特价活动
         *  1：是，0：否
         *  默认0
         */
        String SET_CHECK_SPECIAL_PRICE = "set.check.special.price";

        /**
         *  第三方商品自动下架(可售状态码，英文逗号分隔)
         */
        String SET_THREE_GOODS_AUTO_DOWN = "set.three.goods.auto.down";

        /**
         * 会员卡注册默认类型
         */
        String SET_MEMBER_REGISTER_TYPE = "set.member.register.type";

        /**
         * 商品条码黑名单
         */
        String SET_CODE_BLACKLIST = "set.code.blacklist";

        /**
         * 是否根据门店下传不同包装袋条码下ERP(1：是，0：否)，默认为0
         */
        String SET_SHOP_PACKAGE = "set.shop.package";

        /**
         * 是否订单同步至ERP(1：是，0：否)
         */
        String SET_CORNB_ORDER = "set.cornb.order";

        /**
         * 库存同步模式：0-接口同步可用库存到通信表(推荐)，1-moblink同步账面库存和销售流水到通信表，2-接口同步可用库存到商品库存表。
         */
        String SET_ERP_STOCK_COMMUNICATION_ENABLE = "erp.stock.communication.enable";

        /**
         * 指定店的订单同步至ERP，填写门店第三方门店编号集合
         */
        String SET_ONE_SHOP_CORNB_ORDER = "set.one.shop.cornb.order";

        /**
         * 是否自动解除不存在商品关联条码
         * 1：是
         * 其它值：否
         */
        String SET_AUTO_RELIEVE_NONEXISTEN_CODE = "set.auto.relieve.nonExisten.code";

        /**
         * 京东商家店内分类编号列表(商家店内分类编号列表，商家店内分类分两级，一个商品可以绑定多个店内分类（上传的店内分类需为最末级分类， 即二级店内分类或没有子分类的一级店内分类），店内分类编号通过查询商家店内分类信息接口获取)--默认一个
         *
         */
        String JD_SHOP_CATEGORIES = "jd.shop.categories";

        /**
         * 京东到家类目编号，需传入到家的第三级分类(通过查询到家类目信息接口获取)--默认一个
         *
         */
        String JD_CATEGORY_ID = "jd.category.id";

        /**
         * 京东到家品牌编号(通过分页查询商品品牌信息接口获取)--默认一个
         *
         */
        String JD_BRAND_ID = "jd.brand.id";

        /**
         * 订单详情修改商品编码限制时间
         *
         */
        String UPDATE_GOODSCODE_TIME = "update.goodscode.time";
    }
}
