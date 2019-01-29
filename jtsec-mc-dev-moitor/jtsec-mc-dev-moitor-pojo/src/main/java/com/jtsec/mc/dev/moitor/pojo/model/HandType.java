package com.jtsec.mc.dev.moitor.pojo.model;

public enum HandType {

	TEST("0"),
//	MEM_TOTAL("MEM_TOTAL"),MEM_AVA("MEM_USED"),
	MEM("MEM"),
	CPU("CPU"),
	INTERFACE("INTERFACE"),	//网卡信息
	INTERFACE_EDIT("INTERFACE_EDIT"),	//更新网卡信息
	INTERFACE_IPNET("INTERFACE_IPNET"),
	DISK("DISK"),	//硬盘信息
	MEM_DISK("MEM_DISK"),
	SOFTWARE("SOFTWARE"),SOFTWARE_NAME("SOFTWARE_NAME"),SOFTWARE_TYPE("SOFTWARE_TYPE"),SOFTWARE_TIME("SOFTWARE_TIME"),	//安装的软件
	PROGRESS("PROGRESS"),PROGRESS_REF("PROGRESS_REF"),	//系统进程信息
	SW_ITEM("SW_ITEM"), SW_ITEM_EDIT("SW_ITEM_EDIT");
    private final String value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    HandType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
	
	
}
