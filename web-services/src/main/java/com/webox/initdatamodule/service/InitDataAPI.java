package com.webox.initdatamodule.service;

import com.webox.common.model.RepInitData;
import com.webox.common.model.ReqInitData;

public interface InitDataAPI{
    RepInitData loadInitData(ReqInitData request);
   
}