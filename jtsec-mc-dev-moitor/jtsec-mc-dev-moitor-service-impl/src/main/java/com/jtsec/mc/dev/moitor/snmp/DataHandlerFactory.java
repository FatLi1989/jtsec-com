package com.jtsec.mc.dev.moitor.snmp;

import com.jtsec.mc.dev.moitor.snmp.handler.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 职责链初始化工厂
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class DataHandlerFactory {
	
	private static DataHandlerFactory dataHandlerFactory = null;
	private DataHandlerFactory (){}

	public static synchronized DataHandlerFactory createDataHandlerFactory(){
		if(dataHandlerFactory == null){
			dataHandlerFactory = new DataHandlerFactory();
		}
		return dataHandlerFactory; 
	}
	
	/**
	 * 初始化职责链对象
	 * 
	 * @param ip
	 * @param port
	 * @param monitorTime
	 * @return
	 */
	public AbstractDataHandler getDataHandler(String ip, String port, String monitorTime){
		AbstractDataHandler dataHandler = new DeviceDataHandler (ip, port, monitorTime);
		this.setHandlerSort(dataHandler);
		return dataHandler;
	}
	
	/**
	 * 初始化职责链对象
	 * @Description: 没有实际意义, 只是一个占位符, 区分重载
	 * @param ip
	 * @param port
	 * @param region
	 * @param monitorTime
	 * @param   @return
	 */
	public AbstractDataHandler getDataHandler(String ip, String port, String region, String monitorTime){
		AbstractDataHandler dataHandler = new DeviceDataHandler(ip, port, region, monitorTime);
		this.setHandlerSort(dataHandler);
		return dataHandler;
	}
	
	/**
	 * 将创建的对象置空，尽快释放资源
	 */
	public static void releaseSources(AbstractDataHandler dataHandler){
		if(dataHandler != null){
			AbstractDataHandler adh = dataHandler.getNextHandler();
			releaseSources(adh);
			dataHandler = null;
		}
	}
	
	/**
	 * 设置数据的处理顺序
	 * @param dataHandler
	 */
	private void setHandlerSort(AbstractDataHandler dataHandler){
		AbstractDataHandler cpuMemDataHandler = new CpuMemDataHandler ();
		AbstractDataHandler netFluxDataHandler = new NetFluxDataHandler ();
		AbstractDataHandler servFluxDataHandler =  new ServFluxDataHandler ();
		AbstractDataHandler warnDataHandler = new WarnDataHandler ();
		//设置udp包的处理顺序
		dataHandler.setNextHandler(cpuMemDataHandler);
		cpuMemDataHandler.setNextHandler(netFluxDataHandler);
		netFluxDataHandler.setNextHandler(servFluxDataHandler);
		servFluxDataHandler.setNextHandler(warnDataHandler);
	}
}
