package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class JtFtpFlowDay implements Serializable {

    private Integer id;

    private Date time;

    private String servIp;

    private String servName;

    private Integer fileCount;

    private String direction;

    private Long flowSize;

    private static final long serialVersionUID = 1L;
}