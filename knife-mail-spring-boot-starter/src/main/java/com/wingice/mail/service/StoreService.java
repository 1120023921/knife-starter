package com.wingice.mail.service;

import jakarta.mail.Session;
import jakarta.mail.Store;

public interface StoreService {

    Store getStore(String username);

    Session getSession(String username);
}
