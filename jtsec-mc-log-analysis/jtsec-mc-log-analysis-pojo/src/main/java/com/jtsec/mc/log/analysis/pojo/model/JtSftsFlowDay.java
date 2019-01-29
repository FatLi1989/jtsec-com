package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class JtSftsFlowDay implements Serializable {

    private Integer id;

    private String time;

    private String servIp;

    private String servName;

    public JtSftsFlowDay () {
    }

    public JtSftsFlowDay (String time, String servIp, String servName, Integer fileCount, String direction, Long flowSize) {

        this.time = time;
        this.servIp = servIp;
        this.servName = servName;
        this.fileCount = fileCount;
        this.direction = direction;
        this.flowSize = flowSize;
    }

    private Integer fileCount;

    private String direction;

    private Long flowSize;

    private static final long serialVersionUID = 1L;
}