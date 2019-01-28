package com.webox.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppMessage {
	private String msgId;
	private String message;
	private String msgType; // OK, ERR
	private String dataReturn;
}