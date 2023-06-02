package com.bookstore.usersservice.query.projection;

import com.bookstore.commonservice.model.AddressResponseCommonModel;
import com.bookstore.commonservice.model.ProvinceResponseCommonModel;
import com.bookstore.commonservice.model.UserResponseCommonModel;
import com.bookstore.commonservice.query.GetCommentByUserNameQuery;
import com.bookstore.commonservice.query.GetDetailsAddressQuery;
import com.bookstore.commonservice.query.GetDetailsProvinceQuery;
import com.bookstore.commonservice.query.GetDetailsUserQuery;
import com.bookstore.usersservice.query.model.UserResponseModel;
import com.bookstore.usersservice.query.queries.GetAllUserQuery;
import com.bookstore.usersservice.query.queries.GetUserByIdQuery;
import com.bookstore.usersservice.repository.User;
import com.bookstore.usersservice.repository.UserRepository;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserProjection {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueryGateway queryGateway;

    @QueryHandler
    public UserResponseCommonModel handle(GetDetailsUserQuery getDetailsUserQuery) {
        UserResponseCommonModel model = new UserResponseCommonModel();
        User user = userRepository.getReferenceById(getDetailsUserQuery.getId());
        BeanUtils.copyProperties(user, model);
        return model;
    }

    @QueryHandler
    public List<UserResponseCommonModel> handler(GetCommentByUserNameQuery getCommentByUserNameQuery) {
        List<UserResponseCommonModel> list = new ArrayList<>();
        List<User> users = userRepository.findByFirstNameContainingIgnoreCase(getCommentByUserNameQuery.getName());
        users.forEach(item -> {
            UserResponseCommonModel userResponseCommonModel = new UserResponseCommonModel();
            BeanUtils.copyProperties(item, userResponseCommonModel);
            list.add(userResponseCommonModel);
        });
        return list;
    }

    @QueryHandler
    public List<UserResponseModel> handler(GetAllUserQuery getAllUserQuery) {
        List<UserResponseModel> userResponseModels = new ArrayList<>();
        List<User> users = userRepository.findAll();
        if (users.size() > 0) {
            users.forEach(item -> {
                UserResponseModel model = new UserResponseModel();
                BeanUtils.copyProperties(item, model);
                GetDetailsAddressQuery getDetailsAddressQuery = new GetDetailsAddressQuery(item.getId());
                AddressResponseCommonModel addressResponseCommonModel =
                        queryGateway.query(getDetailsAddressQuery, ResponseTypes.instanceOf(AddressResponseCommonModel.class))
                                .join();
                if (addressResponseCommonModel != null) {
                    model.setAddress(addressResponseCommonModel.getAddressLine1());
                    GetDetailsProvinceQuery getDetailsProvinceQuery
                            = new GetDetailsProvinceQuery(addressResponseCommonModel.getProvinceId());
                    ProvinceResponseCommonModel provinceResponseCommonModel = queryGateway.
                            query(getDetailsProvinceQuery, ResponseTypes.instanceOf(ProvinceResponseCommonModel.class)).join();
                    if (provinceResponseCommonModel != null) {
                        model.setProvince(provinceResponseCommonModel.getName());
                    }
                }

                userResponseModels.add(model);
            });
        }
        return userResponseModels;
    }

    @QueryHandler
    public UserResponseModel handler(GetUserByIdQuery getUserByIdQuery) {
        UserResponseModel model = new UserResponseModel();
        Optional<User> userOptional = userRepository.findById(getUserByIdQuery.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BeanUtils.copyProperties(user, model);
            GetDetailsAddressQuery getDetailsAddressQuery = new GetDetailsAddressQuery(user.getId());
            AddressResponseCommonModel addressResponseCommonModel =
                    queryGateway.query(getDetailsAddressQuery, ResponseTypes.instanceOf(AddressResponseCommonModel.class))
                            .join();
            if (addressResponseCommonModel != null) {
                model.setAddress(addressResponseCommonModel.getAddressLine1());
                GetDetailsProvinceQuery getDetailsProvinceQuery
                        = new GetDetailsProvinceQuery(addressResponseCommonModel.getProvinceId());
                ProvinceResponseCommonModel provinceResponseCommonModel = queryGateway.
                        query(getDetailsProvinceQuery, ResponseTypes.instanceOf(ProvinceResponseCommonModel.class)).join();
                if (provinceResponseCommonModel != null) {
                    model.setProvince(provinceResponseCommonModel.getName());
                }
            }
            return model;
        } else {
            return null;
        }
    }
}
