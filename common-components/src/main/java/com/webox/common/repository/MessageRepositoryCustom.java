package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.MessageCount;

public interface MessageRepositoryCustom {
    public List<MessageCount> unReadMessageCount(String receiver);

}