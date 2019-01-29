package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class JtFtpFlow implements Serializable {

    private Integer id;

    private Date time;

    private String fromIp;

    private String servName;

    private String cliIp;

    private String cliPort;

    private String direction;

    private String fileName;

    private String fileType;

    private String filePath;

    private Integer fileSize;

    private static final long serialVersionUID = 1L;



}