package com.webox.initdatamodule.process;

import com.webox.common.model.RepInitData;
import com.webox.common.model.ReqInitData;
import com.webox.common.utils.AppConsts;
import com.webox.common.utils.AppData;
import com.webox.initdatamodule.service.InitDataAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InitDataImpl implements InitDataAPI {
    private static final Logger logger = LoggerFactory.getLogger(InitDataImpl.class);

	@Override
	public RepInitData loadInitData(ReqInitData request) {
        RepInitData repInitData = new RepInitData();
        repInitData.setCityCode(AppData.cityCode);
        repInitData.setServiceType(AppData.serviceType);
        repInitData.setAppStatus(AppConsts.RETURN_TRUE);
        repInitData.setAppInfo("LOADINITDATA");
        return repInitData;
	}
 
}