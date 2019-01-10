package com.webox.postinfomodule.process;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.process.PostInfoManage;
import com.webox.postinfomodule.service.PostInfoAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PostInfoImpl implements PostInfoAPI {

    private static final Logger logger = LoggerFactory.getLogger(PostInfoImpl.class);

    @Autowired @Qualifier("generalPostInfoManage")
    private PostInfoManage postInfoManage;
   
    @Override
	public Response draftAPostInfo(Request request) {
        return postInfoManage.draftAPostInfo(request);
	}

	@Override
	public Response publishAPostInfo(Request request) {
		return postInfoManage.publishAPostInfo(request);
	}

	@Override
	public Response loadPostInfos(String serviceId, String postInfoStatus) {
    	return postInfoManage.loadPostInfos(serviceId, postInfoStatus);
	}

	@Override
	public Response loadAPostInfo(String postInfoId) {
		return postInfoManage.loadAPostInfo(postInfoId);
	}

	@Override
	public Response deleteAPostInfo(Request request) {
		return postInfoManage.deleteAPostInfo(request);
	}
    
}