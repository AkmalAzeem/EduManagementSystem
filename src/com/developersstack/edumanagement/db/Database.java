package com.developersstack.edumanagement.db;

import com.developersstack.edumanagement.model.Program;
import com.developersstack.edumanagement.model.Student;
import com.developersstack.edumanagement.model.Teacher;
import com.developersstack.edumanagement.model.User;
import com.developersstack.edumanagement.util.security.PasswordManager;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable = new ArrayList();
    public static ArrayList<Student> studentTable = new ArrayList();
    public static ArrayList<Teacher> teacherTable = new ArrayList();
    public static ArrayList<Program> programTable = new ArrayList();

    static {
        userTable.add(
                new User("Akmal","Azeem","ak@gmail.com",
                        new PasswordManager().encrypt("1234"))
        );
    }
}
