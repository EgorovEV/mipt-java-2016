package ru.mipt.java2016.homework.g596.egorov.task4.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.mipt.java2016.homework.base.task1.ParsingException;
import ru.mipt.java2016.homework.g596.egorov.task4.Calculator.MyCalculator;
import ru.mipt.java2016.homework.g596.egorov.task4.database.Application;
import ru.mipt.java2016.homework.g596.egorov.task4.database.UserDB;

import java.util.ArrayList;
import java.util.HashMap;
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

    private boolean existingUser(String username) {
        try {
            calculatorDao.loadUser(username);
            logger.info("Try to read user" + username);
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.info("adding user " + username);
            return false;
        }
}

    @RequestMapping(path = "/ping", method = RequestMethod.GET, produces = "text/plain")
    public String echo() {
        return "You can make sth runnuble, congratulations!\n";
    }

    @RequestMapping(path = "/user/add/{username}", method = RequestMethod.PUT)
    public String addUser(Authentication authentication,
                          @PathVariable String username,
                          @RequestParam String password) {
        if (existingUser(username)) {
            return "This user exists already!";
        }
        String requesterUsername = authentication.getName();
        calculatorDao.addUser(username, password);
        return "User " + username + " successfully created." + '\n';

    }

    @RequestMapping(path = "user/{username}", method = RequestMethod.GET)
    public UserDB checkUser(@PathVariable("username") String username) {
        return calculatorDao.loadUser(username);
    }


    @RequestMapping(path = "user/{username}/calc", method = RequestMethod.PUT)
    public void addCalculations(@PathVariable String username, @RequestBody String toCalculate) {
        try {
            logger.info("___I try to calculate:___" + toCalculate);
            String CalcHistory = toCalculate + '=' + Double.toString(MyCalculator.calculate(toCalculate));
            userCalculators.put(username, CalcHistory);
        } catch (ParsingException e) {
            logger.info("_____________!MISTAKE!_______________");
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "user/{username}/history", method = RequestMethod.GET)
    public ArrayList<String> showHistory(@PathVariable String username){
        ArrayList ans = new ArrayList();
        for(Map.Entry<String, String> e : userCalculators.entrySet()) {
            if(e.getKey().equals(username)) {
                ans.add(e.getValue());
            }
        }
        return ans;
    }
    //сделать новый калькулятор
}