package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * [STRATO MyBatis Generator]
 * Table: agent_user_info
@mbggenerated do_not_delete_during_merge 2018-11-19 09:54:33
 */
@Data
public class AgentUserInfo implements Serializable {
    /**
     * Column: agent_user_info.id
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer id;

    /**
     * Column: agent_user_info.user_name
    @mbggenerated 2018-11-19 09:54:33
     */
    private String userName;

    /**
     * Column: agent_user_info.password
    @mbggenerated 2018-11-19 09:54:33
     */
    private String password;

    /**
     * Column: agent_user_info.user_desc
    @mbggenerated 2018-11-19 09:54:33
     */
    private String userDesc;

    /**
     * Column: agent_user_info.cert_id
    @mbggenerated 2018-11-19 09:54:33
     */
    private String certId;

    /**
     * Column: agent_user_info.ip_mac
    @mbggenerated 2018-11-19 09:54:33
     */
    private String ipMac;

    /**
     * Column: agent_user_info.login_type
    @mbggenerated 2018-11-19 09:54:33
     */
    private String loginType;

    /**
     * Column: agent_user_info.group_name
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer groupName;

    /**
     * Column: agent_user_info.filter_rule
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer filterRule;

    /**
     * Column: agent_user_info.audit_rule
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer auditRule;

    /**
     * Column: agent_user_info.flow_rule
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer flowRule;

    /**
     * Column: agent_user_info.quota_rule
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer quotaRule;

    /**
     * Column: agent_user_info.auto_flow_ctl
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer autoFlowCtl;

    /**
     * Column: agent_user_info.min_up
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer minUp;

    /**
     * Column: agent_user_info.min_down
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer minDown;

    /**
     * Column: agent_user_info.max_up
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer maxUp;

    /**
     * Column: agent_user_info.max_down
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer maxDown;

    /**
     * Column: agent_user_info.wan
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer wan;

    /**
     * Column: agent_user_info.connset
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer connset;

    /**
     * Column: agent_user_info.conn
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer conn;

    /**
     * Column: agent_user_info.con_tcp_conn
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer conTcpConn;

    /**
     * Column: agent_user_info.con_udp_conn
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer conUdpConn;

    /**
     * Column: agent_user_info.allow_time
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer allowTime;

    /**
     * Column: agent_user_info.allow_multi
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer allowMulti;

    /**
     * Column: agent_user_info.auto_bind_mac
    @mbggenerated 2018-11-19 09:54:33
     */
    private String autoBindMac;

    /**
     * Column: agent_user_info.online
    @mbggenerated 2018-11-19 09:54:33
     */
    private String online;

    /**
     * Column: agent_user_info.login_time
    @mbggenerated 2018-11-19 09:54:33
     */
    private Date loginTime;

    /**
     * Column: agent_user_info.priority
    @mbggenerated 2018-11-19 09:54:33
     */
    private String priority;

    /**
     * Column: agent_user_info.rate
    @mbggenerated 2018-11-19 09:54:33
     */
    private Integer rate;

    /**
     * Table: agent_user_info
    @mbggenerated 2018-11-19 09:54:33
     */
    private static final long serialVersionUID = 1L;
}