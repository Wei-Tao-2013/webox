package com.webox.common.role;

import com.webox.common.model.Contact;

public abstract class Customer{
    private Contact contact;

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

}