package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.webox.common.model.HotSpot;
import com.webox.common.model.Response;
import com.webox.common.repository.HotSpotRepository;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.utils.AppConsts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component("hotSpotManage")
public class HotSpotManage {
    private static final Logger logger = LoggerFactory.getLogger(HotSpotManage.class);

    @Autowired
    HotSpotRepository hotSpotRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public HotSpot getHotSpot(String hotSpotId) {
        HotSpot hotSpot = hotSpotRepository.findByHotSpotId(hotSpotId);
        return hotSpot;
    }

    public HotSpot toggleHotSpot(String userId, String serviceId) {
        HotSpot hotSpot = new HotSpot(userId, serviceId);
        Example<HotSpot> example = Example.of(hotSpot);
        LocalDateTime localDateTime = LocalDateTime.now();
        Optional<HotSpot> hotSpotOptional = hotSpotRepository.findOne(example);
        if (hotSpotOptional.isPresent()) {
            hotSpot = hotSpotOptional.get();
            hotSpot.setStatus(!hotSpot.getStatus());
        } else {
            hotSpot.setStatus(true);
        }
        hotSpot.setUpdateTime(localDateTime);
        hotSpotRepository.save(hotSpot);
        return hotSpot;
    }

    public Response toggleHotSpotList(String userId, List<String> serviceIdList) {
        Response response = new Response();
        List<HotSpot> hotSpotList = new ArrayList<HotSpot>();
        serviceIdList.forEach(serviceId -> {
            hotSpotList.add(this.toggleHotSpot(userId, serviceId));
        });
        response.setAppStatus(AppConsts.RETURN_TRUE);
        response.setAppInfo("toggle Hot Spot");
        response.setHotSpotList(hotSpotList);
        return response;
    }

    public Response loadHotSpotService(String userId) {
        Response response = new Response();
        response.setServices(hotSpotRepository.loadHotSpotServicesByUser(userId));
        response.setAppInfo("hotspot service loaded");
        response.setAppStatus(AppConsts.RETURN_TRUE);
        return response;
    }

    /*
     * public Response loadHotSpotService(String userId) { Response response = new
     * Response(); List<HotSpot> hotspotlist =
     * hotSpotRepository.findByUserId(userId); List<Service> serviceList = new
     * ArrayList<Service>();
     * hotspotlist.sort((a,b)->a.getRank().compareTo(b.getRank())); //
     * userList.forEach(user->{user.getUserAccount().forEach(account->accountList.
     * add(account));}); hotspotlist.forEach(o->{ if(o.getStatus()){ Service service
     * = serviceRepository.findByServiceId(o.getServiceId());
     * serviceList.add(service); } }); response.setServices(serviceList);
     * response.setAppInfo("hotspot service loaded");
     * response.setAppStatus(AppConsts.RETURN_TRUE); return response; }
     */
}