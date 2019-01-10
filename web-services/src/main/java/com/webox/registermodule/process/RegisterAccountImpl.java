package com.webox.registermodule.process;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

import com.webox.common.process.AccountRegister;
import com.webox.common.process.JWToken;
import com.webox.common.utils.AppConsts;
import com.webox.registermodule.service.RegisterAccountAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RegisterAccountImpl implements RegisterAccountAPI {
    private static final Logger logger = LoggerFactory.getLogger(RegisterAccountImpl.class);

    @Autowired @Qualifier("weixinAccount")
    private AccountRegister weiXinAccountRegister;
    @Autowired
    private JWToken jwtoken;

    @Override
	public Response weixinRegister(Request request) {
       return  weiXinAccountRegister.register(request);
	}

	@Override
	public Response weixinSignAccount(Request request) {
        return  weiXinAccountRegister.signAccount(request);
    }

    @Override
	public Response weixinSignOut(Request request) {
        return weiXinAccountRegister.signOut(request);
    }

    @Override
	public Response refreshToken(Request request) {
       return jwtoken.generateToken(request.getTokenSeed());
    }

	@Override
	public Response weixinProfileUpdate(Request request) {
        return weiXinAccountRegister.profileUpdate(request);
	}

	@Override
	public Response loadWeiXinInfo(Request request) {
        Response response = new Response();
        response.setUser(weiXinAccountRegister.loadUserInfo(request.getUserId()));
        response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}
    
}