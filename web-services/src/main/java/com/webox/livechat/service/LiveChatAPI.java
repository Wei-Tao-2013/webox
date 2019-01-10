package com.webox.livechat.service;

import java.util.List;

import com.webox.common.model.MessageCount;
import com.webox.common.model.User;

public interface LiveChatAPI{
    List<MessageCount> loadUnreadMsgCount(String receiver);
    List<User> loadUserList(String receiver);
    List<MessageCount> loadMessageAccount(String receiver);
 
}