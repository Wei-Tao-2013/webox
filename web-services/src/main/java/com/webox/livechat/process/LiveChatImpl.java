package com.webox.livechat.process;

import java.util.List;

import com.webox.common.model.MessageCount;
import com.webox.common.model.User;
import com.webox.common.process.MessageManage;
import com.webox.livechat.service.LiveChatAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LiveChatImpl implements LiveChatAPI {

    private static final Logger logger = LoggerFactory.getLogger(LiveChatImpl.class);

    @Autowired @Qualifier("messageManage")
    private MessageManage messageMannage;

	@Override
	public List<MessageCount> loadUnreadMsgCount(String receiver) {
		return messageMannage.loadUnreadMsgCount(receiver);
	}

	@Override
	public List<User> loadUserList(String receiver) {
		return null;
	}

	@Override
	public List<MessageCount> loadMessageAccount(String receiver) {
		return messageMannage.loadMessageAccount(receiver);
	}

}