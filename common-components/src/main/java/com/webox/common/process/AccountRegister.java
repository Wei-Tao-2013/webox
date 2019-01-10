package com.webox.common.process;

import java.util.List;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.User;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.repository.UserRepository;
import com.webox.common.utils.AppConsts;
import com.webox.common.utils.GIutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AccountRegister {

  private static final Logger logger = LoggerFactory.getLogger(AccountRegister.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  ServiceRepository serviceRepository;

  public abstract Response register(Request request);

  public abstract Response signAccount(Request request);

  public abstract Response signOut(Request request);

  public abstract User loadUserInfo(String userId);


  public Response profileUpdate(Request request) {
    Response response = new Response();
    User requestUser = request.getUser();
    logger.debug("request {} ", GIutils.converToJson(request));
    logger.debug("Useris {} ", GIutils.converToJson(requestUser));
    if (requestUser != null) {
      logger.debug("User email address is {} ", requestUser.getPrimaryEmail());
      List<User> userList = userRepository.findByPrimaryEmail(requestUser.getPrimaryEmail());
      if (userList.isEmpty()) {
        response.setAppCode(AppConsts.REG_USERNOTFOUND);
      } else {
        User existUser = (User) userList.get(0);
        requestUser.setUserId(existUser.getUserId());
        userRepository.save(requestUser);
        response.setAppCode(AppConsts.REG_USEUPDATED);
      }
    } else {
      response.setAppCode(AppConsts.REG_USERNOTFOUND);
    }
    response.setAppStatus(AppConsts.RETURN_TRUE);
    return response;
  }

}