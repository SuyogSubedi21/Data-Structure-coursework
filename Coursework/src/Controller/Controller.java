/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Suyoug Subedi
 */
public class Controller {
 

   
 public String authenticate(String username, String password) {

        if (username == null) username = "";
        if (password == null) password = "";

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() && password.isEmpty()) {
            return "ERROR: Username and password are required.";
        }

        if (username.isEmpty()) {
            return "ERROR: Username is required.";
        }

        if (password.isEmpty()) {
            return "ERROR: Password is required.";
        }

        if (username.equals("admin1") && password.equals("admin123")) {
            return "ADMIN";
        }

        if (password.equals("user123")) {
            for (int i = 1; i <= 10; i++) {
                if (username.equals("user" + i)) {
                    return "USER";
                }
            }
        }

        return "ERROR: Invalid username or password.";
    }
}