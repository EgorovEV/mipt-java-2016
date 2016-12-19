package ru.mipt.java2016.homework.g596.egorov.task4.users.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import java.util.Collections;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
}
