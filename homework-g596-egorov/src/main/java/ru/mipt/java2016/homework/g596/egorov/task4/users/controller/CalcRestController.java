package ru.mipt.java2016.homework.g596.egorov.task4.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mipt.java2016.homework.g596.egorov.task4.Calculator.FuncCalc;
import ru.mipt.java2016.homework.g596.egorov.task4.database.Application;
import ru.mipt.java2016.homework.g596.egorov.task4.database.UserDB;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalcRestController {
    private static final Logger logger =
            LoggerFactory.getLogger(CalcRestController.class.getName());
    @Autowired
    private Application calculatorDao;
    @Autowired
    private static Map<String, String> userCalculators = new HashMap<>();

    @RequestMapping(path = "/ping", method = RequestMethod.GET, produces = "text/plain")
    public String echo() {
        return "You can make sth runnuble, congratulations!\n";
    }

    @RequestMapping(path = "user/add/", method = RequestMethod.PUT)
    public String createUser(@RequestBody UserDB user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User " + user.getUsername());
        if (!(user.getid() == 0)) {
            return "You are not an admin. Cannot add users." + '\n';
        } else {    //посмотри в application найцди нужные методы
            if (calculatorDao.addUserDao(username, password, true)) {
                userCalculators.put(username, new RESTCalculator());
                return "User " + username + " successfully created." + '\n';
            } else {
                return "User " + username + " already exists." + '\n';
            }
        }
    }
        //Пусть выводит нужного юззера
    @RequestMapping(path = "user/{username}", method = RequestMethod.PUT)
    public String checkUser(@PathVariable("id") long id) {
        logger.info("Cheching User with id" + id);

    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error spittleNotFound(UserNotFoundException e) {
        long userId = e.getUserId();
        return new Error(4, "User [" + userId + "] not found");
    }

    //сделать загрузку строки в usercalculators, где строка будет передаваться
    //калькулятору из первого задания.
    //сделать админку
    //сделать новый калькулятор
    
}