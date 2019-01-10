package com.webox.servicemodule.process;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.process.ServiceManage;
import com.webox.servicemodule.service.ServiceAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ServiceImpl implements ServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired @Qualifier("generalServiceManage")
    private ServiceManage serviceManage;
   
    @Override
	public Response draftAService(Request request) {
        return serviceManage.draftAService(request);
	}

	@Override
	public Response registerAService(Request request) {
		return serviceManage.registerAService(request);
	}

	@Override
	public Response loadServices(String userId, String serviceStatus) {
    	return serviceManage.loadServices(userId,serviceStatus);
	}

	@Override
	public Response loadAService(String serviceId) {
		return serviceManage.loadAService(serviceId);
	}

	@Override
	public Response loadAService(String serviceId, String userId) {
		return serviceManage.loadAService(serviceId,userId);
	}

	@Override
	public Response searchingService(String searchingWords) {
		return null;
	}
    
}