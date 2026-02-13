package com.financeTracker.app.manager;

import com.financeTracker.dao.ExpenseDAO;
import com.financeTracker.model.Expense;
import com.financeTracker.dao.UserDAO;
import com.financeTracker.model.User;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class ExpenseManager {
    ExpenseDAO expenseDAO = new ExpenseDAO();
    UserDAO userDAO = new UserDAO();

    public Expense new_expense(int user_id, BigDecimal amount, String label, String description, Timestamp date){
        Expense expense = new Expense(user_id, amount, label, description, date);
        User user = userDAO.findById(user_id).orElse(null);
        assert user != null;
        expenseDAO.create(expense);
        // update the user's balance
        user.setBalance(user.getBalance().add(amount));
        userDAO.update(user);
        return expense;
    }

    public List<Expense> all_expenses(int user_id){
        return expenseDAO.listByUser(user_id);
    }
}
