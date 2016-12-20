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
import ru.mipt.java2016.homework.base.task1.ParsingException;
import ru.mipt.java2016.homework.g596.egorov.task4.Calculator.FuncCalc;
import ru.mipt.java2016.homework.g596.egorov.task4.Calculator.MyCalculator;
import ru.mipt.java2016.homework.g596.egorov.task4.database.Application;
import ru.mipt.java2016.homework.g596.egorov.task4.database.UserDB;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalcRestController {
    private MyCalculator MyCalculator = new MyCalculator();

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

    @RequestMapping(path = "/user/add/{username}", method = RequestMethod.PUT)
    public String addUser(Authentication authentication,
                          @PathVariable String username,
                          @RequestParam String password) {
        String requesterUsername = authentication.getName();

        if (!requesterUsername.equals(requesterUsername)) {
            return "You are not an admin. Cannot add users." + '\n';
        } else {
            calculatorDao.addUser(username, password);
                return "User " + username + " successfully created." + '\n';
        }
    }

    @RequestMapping(path = "user/{username}", method = RequestMethod.GET)
    public UserDB checkUser(@PathVariable("username") String username) {
        return calculatorDao.loadUser(username);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error spittleNotFound(UserNotFoundException e) {
        long userId = e.getUserId();
        return new Error(4, "User [" + userId + "] not found");
    }

    @RequestMapping(path = "user/{username}/calc", method = RequestMethod.PUT)
    public void addCalculations(@PathVariable String username,
                                @RequestParam String toCalculate) {
        logger.info("_____________START_______________");
        try {
            logger.info("_____________IN TRY_______________");
            String CalcHistory = toCalculate + '=' + Double.toString(MyCalculator.calculate(toCalculate));
            userCalculators.put(username, CalcHistory);
        } catch (ParsingException e) {
            logger.info("_____________IN CATCH_______________");
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "user/{username}/history", method = RequestMethod.GET)
    public List<String> showHistory(@PathVariable String username){
        
    }


    //сделать загрузку строки в usercalculators, где строка будет передаваться
    //калькулятору из первого задания.
    //сделать админку
    //сделать новый калькулятор

}