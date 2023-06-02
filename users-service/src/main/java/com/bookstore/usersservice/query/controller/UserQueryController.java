package com.bookstore.usersservice.query.controller;


import com.bookstore.usersservice.query.model.UserResponseModel;
import com.bookstore.usersservice.query.queries.GetAllUserQuery;
import com.bookstore.usersservice.query.queries.GetUserByIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/listUser")
    public List<UserResponseModel> getAllUser() {
        GetAllUserQuery getAllUserQuery = new GetAllUserQuery();
        List<UserResponseModel> list = queryGateway.query(getAllUserQuery,
                ResponseTypes.multipleInstancesOf(UserResponseModel.class)).join();
        return list;
    }

    @GetMapping("/userId")
    public UserResponseModel getUserById(@RequestParam(required = false) Long userId) {
        GetUserByIdQuery getUserByIdQuery = new GetUserByIdQuery(userId);
        UserResponseModel userResponseModel = queryGateway.query(getUserByIdQuery,
                ResponseTypes.instanceOf(UserResponseModel.class)).join();
        return userResponseModel;
    }
}
