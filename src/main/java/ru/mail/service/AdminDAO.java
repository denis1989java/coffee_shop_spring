package ru.mail.service;

import ru.mail.service.model.Admin;

public interface AdminDAO {
    Admin loadUserByUsername(String username);
}
