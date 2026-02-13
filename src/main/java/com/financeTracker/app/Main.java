package com.financeTracker.app;

import com.financeTracker.app.manager.UserManager;
import com.financeTracker.app.manager.ExpenseManager;
import com.financeTracker.dao.UserDAO;
import com.financeTracker.model.Expense;
import com.financeTracker.model.User;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(){
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();
        ExpenseManager expenseManager = new ExpenseManager();
        UserDAO userDAO = new UserDAO();
        boolean user_status = false;
        Optional<User> user = Optional.empty();

        print("Finance Tracker");

        //Prompt user to signup/login
        while (!user_status){
            print("[L]og-in or [S]ign-up?");
            String userInput = scanner.nextLine();
            String first_name;
            String last_name;
            String username;
            String password;
            switch (userInput.toLowerCase()){
                //signup
                case "s":
                    print("First Name:");
                    first_name = scanner.nextLine();
                    print("Last Name:");
                    last_name = scanner.nextLine();
                    print("Username:");
                    username = scanner.nextLine();
                    print("Password:");
                    password = scanner.nextLine();
                    user = userManager.signUp(username, password, first_name, last_name);
                    assert user.orElse(null) != null;
                    user_status = true;
                    break;
                case "l":
                    //login
                    print("Username:");
                    username = scanner.nextLine();
                    print("Password:");
                    password = scanner.nextLine();
                    user = userManager.logIn(username, password);
                    assert user.orElse(null) != null;
                    user_status = true;
                    break;
                default:
                    print("Invalid Input. Try Again");
                    break;
            }}

        while (user_status){
            User active_user = userDAO.findById(user.orElse(null).getId()).orElse(null);
            //display account stats
            print("Hello " + active_user.getFirst_name() + " " + active_user.getLast_name());
            print(active_user.getBalance().toPlainString());
            print("==========RECENT TRANSACTIONS==========");
            List<Expense> all_expenses = expenseManager.all_expenses(user.get().getId());
            int limit = Math.min(all_expenses.size(), 3);
            for (int i = 0; i < limit; i++) {
                Expense expense = all_expenses.get(i);

                print("Label: " + expense.getLabel());
                print("Amount: " + expense.getAmount().toPlainString());
                print("Description: " + expense.getDescription());
                print("Date: " + expense.getDate().toString());
                print("--------------------------------");
            }

            //Account commands
            print("[L]og Transaction, [V]iew all Transactions, or [S]ign-out?");
            String userInput = scanner.nextLine();
            switch (userInput.toLowerCase()){
                case "l":
                    print("[D]eposit or [E]xpense?");
                    userInput = scanner.nextLine();
                    String label;
                    String description;
                    BigDecimal amount;
                    switch (userInput.toLowerCase()){
                        case "d":
                            print("Label:");
                            label = scanner.nextLine();
                            print("Description:");
                            description = scanner.nextLine();
                            print("Amount:");
                            amount = scanner.nextBigDecimal();
                            scanner.nextLine();
                            expenseManager.new_expense(active_user.getId(), amount, label, description, new Timestamp(System.currentTimeMillis()));
                            break;
                        case "e":
                            print("Label:");
                            label = scanner.nextLine();
                            print("Description:");
                            description = scanner.nextLine();
                            print("Amount:");
                            amount = scanner.nextBigDecimal().negate();
                            scanner.nextLine();
                            expenseManager.new_expense(active_user.getId(), amount, label, description, new Timestamp(System.currentTimeMillis()));
                            break;
                    }

                case "v":
                    all_expenses.forEach(expense -> {
                        print(expense.getLabel());
                        print("Label: " + expense.getLabel());
                        print("Amount: " + expense.getAmount().toPlainString());
                        print("Description: " + expense.getDescription());
                        print("Date: " + expense.getDate().toString());
                        print("--------------------------------");
                    });
                    break;
                case "s":
                    user_status = false;
                    user = Optional.empty();
                    break;
                default:
                    print("Invalid Input. Try Again");
            }
        }
    }

    //this feels like cheating ngl
    public static void print(String x){
        System.out.println(x);
    }
}
