package com.webox.common.process;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface ServiceRequestManage {

    /* response a quoto to the request */
    public Response acceptAServiceRequest(Request request);

    /* generate a order to the request */
    public Response acceptAServiceQuoto(Request request);

}