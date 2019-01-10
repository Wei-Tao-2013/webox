package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.webox.common.model.Photo;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.Service;
import com.webox.common.model.User;
import com.webox.common.model.WatchList;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.repository.UserRepository;
import com.webox.common.repository.WatchListRepository;
import com.webox.common.utils.AppConsts;
import com.webox.common.utils.AppData;
import com.webox.common.utils.GIutils;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public abstract class ServiceManage {
  private static final Logger logger = LoggerFactory.getLogger(ServiceManage.class);

  private ServiceRequestManage serviceRequestManage;

  @Autowired
  ServiceRepository serviceRepository;

  @Autowired
  UserRepository userRepository;
  @Autowired
  WatchListRepository watchListRepository;

  @Autowired
  @Qualifier("fileStore")
  FileStore fileStore;

  //private LocalDateTime localDateTime = LocalDateTime.now();

  public Response draftAService(Request request) {
    LocalDateTime localDateTime = LocalDateTime.now();
    Service service = new Service();
    Response response = new Response();
    service = request.getService();
    String serviceId = service.getServiceId();
    Service existService = serviceRepository.findByServiceId(serviceId);

    if (existService != null) {
      List<Photo> photoList = existService.getServicePhoto();
      GIutils.copyProperties(service, existService);
      existService.setServicePhoto(photoList);
      existService.setStatus("DRAFT");
      existService.setUpdatedTime(localDateTime);

      logger.debug("draft existService is " + GIutils.converToJson(existService));
      serviceRepository.save(existService);
      serviceId = existService.getServiceId();
    } else {
      service.setStatus("DRAFT");
      service.setUpdatedTime(localDateTime);
      service.setServiceRegisterDate(localDateTime);
      serviceRepository.save(service);
      serviceId = service.getServiceId();
    }

    response.setAppInfo("DRAFT");
    response.setAppStatus(AppConsts.RETURN_TRUE);
    response.setServiceId(serviceId);
    logger.debug("draft Service is update time is " + localDateTime);
    return response;
  }

  public Response loadServices(String userId, String serviceStatus) {
    Response response = new Response();
    response.setAppInfo("SERVICESLOADED");
    response.setAppStatus(AppConsts.RETURN_TRUE);
    if ("ALL".equalsIgnoreCase(serviceStatus)) {
      response.setServices(serviceRepository.findByUserId(userId));
    } else {
      response.setServices(serviceRepository.findByUserId(userId).stream()
          .filter(o -> o.getStatus().equalsIgnoreCase(serviceStatus)).collect((Collectors.toList())));
    }
    response.setUsers(this.getVendorListbyServiceList(response.getServices()));
    return response;
  }

  // also check if service has been watched by userid
  public Response loadAService(String serviceId, String userId) {
    Response response = new Response();
    Service service = serviceRepository.findByServiceId(serviceId);
    if (service != null) {
      response.setAppInfo("ASERVICELOADED");
      response.setUser(this.maskSencitiveInfo(this.getVendorbyService(service)));

      WatchList watchList = new WatchList(userId, serviceId);
      watchList.setStatus(true);
      ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("rank");
      Example<WatchList> example = Example.of(watchList, matcher);
      Optional<WatchList> WatchListOptional = watchListRepository.findOne(example);
      response.setBeWatched(WatchListOptional.isPresent());
    } else {
      response.setAppInfo("SERVICENOTFOUND");
    }
    response.setService(service);
    response.setAppStatus(AppConsts.RETURN_TRUE);
    return response;
  }

  public Response loadAService(String serviceId) {
    Response response = new Response();
    Service service = serviceRepository.findByServiceId(serviceId);
    if (service != null) {
      response.setAppInfo("ASERVICELOADED");
      response.setUser(this.maskSencitiveInfo(this.getVendorbyService(service)));
    } else {
      response.setAppInfo("SERVICENOTFOUND");
    }
    response.setService(service);
    response.setAppStatus(AppConsts.RETURN_TRUE);
    return response;
  }

  public List<Service> searchServicesByTag(){
    return serviceRepository.searchServiceByTag();
  }

  public Response loadHotSpotServicesForUser(String userId) {
    
    Response response = new Response();
    response.setAppStatus(AppConsts.RETURN_TRUE);
    List<Service> services = serviceRepository.loadHotSpotServicesForUser(userId);
    // User user = userRepository.findByUserId(userId);
    // if (user != null) {
    // String userCity = user.getAddress().getCity();
    // }
    // services.stream().filter(s->s.getAddress().getCity().equalsIgnoreCase(anotherString))
    if (services.isEmpty()) {
      response.setAppInfo("SERVICESNOTFOUND");
      response.setServices(services);
      response.setUsers(this.getVendorListbyServiceList(response.getServices()));
    } else {
      response.setAppInfo("SERVICESLOADED");
    }
    response.setServices(services);
    response.setUsers(this.getVendorListbyServiceList(response.getServices()));
    return response;
  }

  public List<User> getVendorListbyServiceList(List<Service> serviceList) {
    List<User> userList = new ArrayList<User>();
    if (!serviceList.isEmpty()) {
      serviceList.forEach(o -> {
        User user = userRepository.findByUserId(o.getUserId());
        this.maskSencitiveInfo(user);
        userList.add(user);
      });
    }
    return userList;
  }

  private User getVendorbyService(Service service) {
    return userRepository.findByUserId(service.getUserId());
  }

  private User maskSencitiveInfo(User user) {
    user.setDateofBirth(null);
    user.setSocialID("######");
    user.setPersnalGreetings("######");
    user.setRefVendorId("######");
    user.setPersnalPhoto("######");
    user.setRegisterTime(null);
    user.setUserRole("######");
    user.setPrimaryEmail("######");
    user.getUserAccount().forEach(o -> {
      o.setAccCreateTime(null);
      o.setAccStatus("#######");
      o.setLastTimeLogout(null);
      o.setAccountId("######");
      o.setLastTimeLogin(null);
      o.setOpenId("######");
      o.setLastTimeLogin(null);
      o.setVisitNumber(999999);
    });

    return user;
  }

  public Response registerAService(Request request) {
    LocalDateTime localDateTime = LocalDateTime.now();
    Service service = new Service();
    Response response = new Response();
    service = request.getService();
    String serviceId = service.getServiceId();

    Service existService = serviceRepository.findByServiceId(serviceId);
    if (existService != null) {
      List<Photo> photoList = existService.getServicePhoto();
      GIutils.copyProperties(service, existService);
      existService.setServicePhoto(photoList);

      existService.setStatus("FORAPPROVAL");
      existService.setUpdatedTime(localDateTime);
      logger.debug("for approval  existService is " + GIutils.converToJson(existService));
      serviceRepository.save(existService);
      serviceId = existService.getServiceId();
    } else {
      service.setStatus("FORAPPROVAL");
      service.setServiceRegisterDate(localDateTime);
      service.setUpdatedTime(localDateTime);
      serviceRepository.save(service);
      serviceId = service.getServiceId();
    }

    response.setServiceId(serviceId);
    /*
     * //this.loadServices(request.getService().getUserId()); //List<Service>
     * aService =
     * this.serviceList.stream().filter(sers->sers.getServiceCode().equalsIgnoreCase
     * (serviceCode)).collect(Collectors.toList()); if (aService.isEmpty()){
     * serviceRepository.save(service); }else{ // update existing service Service
     * existService = (Service)aService.get(0); logger.debug("existService" +
     * GIutils.converToJson(existService)); if
     * (existService.getStatus().equalsIgnoreCase("DRAFT")){
     * service.setServiceId(existService.getServiceId());
     * serviceRepository.save(service); }else{ response.setAppInfo("EXISTNOTDRAFT");
     * // the service has been in process can't be updated at this stage
     * response.setAppInfo(AppConsts.RETURN_TRUE); } }
     */
    response.setAppInfo("FORAPPROVAL");
    response.setAppStatus(AppConsts.RETURN_TRUE);
    return response;
  }

  public abstract Response storeImage(Request request, String fileName, Binary data);

  public abstract Response getImages(String serviceId);

  public abstract void deleteImage(String fileId);

  public Response approvalAService(Request request) {
    return null;
  }

  Response rejectAService(Request request) {
    return null;
  }

  Response blockAService(Request request) {
    return null;
  }

  Response acceptAServiceRequest(Request request) {
    return serviceRequestManage.acceptAServiceRequest(request);
  }

  Response acceptAServiceQuoto(Request request) {
    return serviceRequestManage.acceptAServiceQuoto(request);
  }

  public void fillupSearchingMatrix() {
    List<Service> services = this.searchServicesByTag();
   // HashMap<String, String> servicesMap = new HashMap<String,String>();
    services.forEach(s->{
       AppData.searchingService.put(s.getServiceId(),s.getKeywords());
    });
   // AppData.searchingMatrix.put("services",servicesMap);
  }


}