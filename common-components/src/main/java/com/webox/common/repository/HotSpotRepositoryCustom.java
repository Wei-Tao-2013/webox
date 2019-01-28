package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.Service;

public interface HotSpotRepositoryCustom {
    public List<Service> loadHotSpotServicesByUser(String userId);

}