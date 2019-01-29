package com.jtsec.common.util.json;

import com.alibaba.fastjson.JSONObject;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/11 14:52
 */
public class JsonConvert {

		public static JSONObject ConvertToObject(String json){
				return JSONObject.parseObject (json);
		}

}
