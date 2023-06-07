package com.tutorial.demo.user;

/*
   PERMISSIONS for each role:
   Admin - all permissions
   Student - can only read
*/

public enum UserPermission {
    STUDENT_READ("student:read"),               // read/write to students info DB
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),                 // read/write to course info DB
    COURSE_WRITE("course:write");


    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
