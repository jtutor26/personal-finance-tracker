package com.financeTracker.app.manager;

import com.financeTracker.dao.UserDAO;
import com.financeTracker.model.User;

import java.math.BigDecimal;
import java.util.Optional;

public class UserManager {
    UserDAO dao = new UserDAO();
    //log in
    public Optional<User> logIn(String username, String password) {
        Optional<User> user_box = dao.findByUsername(username);
        User user = user_box.orElse(null);
        if ((user != null) && (user.getPassword().equals(password))){
                return Optional.of(user);
        }
        else {return Optional.empty();}
    }
    //sign up
    public Optional<User> signUp(String username, String password, String first_name, String last_name){
        BigDecimal initial_balance = new BigDecimal("0.00");
        User new_user = new User(username, password, first_name, last_name,initial_balance);
        int user_id = dao.create(new_user);

        Optional<User> user_box = dao.findById(user_id);
        User user = user_box.orElse(null);
        if ((user != null)){
            return Optional.of(user);
        }
        else {return Optional.empty();}
    }
    //check log in status
}
