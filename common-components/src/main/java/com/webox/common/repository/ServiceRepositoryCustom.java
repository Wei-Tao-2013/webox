package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.Service;
public interface ServiceRepositoryCustom {
    
    public void deleteNestedPhoto(String fileId);

    public List<Service> loadHotSpotServicesForUser(String userId);

    public List<Service> searchServiceByTag();
}