package com.webox.common.process;

import java.util.List;

import com.webox.common.model.ServiceType;
import com.webox.common.repository.ServiceTypeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("serviceTypeManage")
public class ServiceTypeManage {
    private static final Logger logger = LoggerFactory.getLogger(ServiceTypeManage.class);

    @Autowired
    ServiceTypeRepository serviceTypeRepository;

    public ServiceType getServiceType(String typeName) {
        return serviceTypeRepository.findByTypeName(typeName).get(0); // in case of typeName being not unique
    }

    public List<ServiceType> loadServiceTypes() {
        return serviceTypeRepository.findAll();
    }

}