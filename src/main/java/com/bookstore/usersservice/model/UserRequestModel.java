package com.bookstore.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestModel {

    private Long id;
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String province;
    private Boolean enabled;

}
