package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class JtSftsFlowHour implements Serializable {

    private Integer id;

    private String time;

    private String servIp;

    private String servName;

    private Integer fileCount;

    private String direction;

    private Long flowSize;

    //添加属性
    private String startTime;

    public JtSftsFlowHour (String direction, String startTime, String endTime) {
        this.direction = direction;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String endTime;

    private static final long serialVersionUID = 1L;

    public JtSftsFlowHour () {
    }

    public JtSftsFlowHour (String time, String servIp, String servName, Integer fileCount, String direction, Long flowSize) {

        this.time = time;
        this.servIp = servIp;
        this.servName = servName;
        this.fileCount = fileCount;
        this.direction = direction;
        this.flowSize = flowSize;
    }
}