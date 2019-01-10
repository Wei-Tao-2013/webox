package com.webox.hotspotmodule.process;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.process.HotSpotManage;
import com.webox.common.process.ServiceManage;
import com.webox.hotspotmodule.service.HotSpotAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HotSpotImpl implements HotSpotAPI {

    private static final Logger logger = LoggerFactory.getLogger(HotSpotImpl.class);

    @Autowired @Qualifier("hotSpotManage")
	private HotSpotManage hotSpotManage;
	
	@Autowired @Qualifier("generalServiceManage")
    private ServiceManage serviceManage;
   
    @Override
	public Response toggleHotSpotList(Request request) {
        return hotSpotManage.toggleHotSpotList(request.getUserId(), request.getServiceIdList());
	}

	@Override
	public Response loadHotSpotServices(Request request) {
		//return hotSpotManage.loadHotSpotService(request.getUserId());
		return serviceManage.loadHotSpotServicesForUser(request.getUserId());

		
	}

}