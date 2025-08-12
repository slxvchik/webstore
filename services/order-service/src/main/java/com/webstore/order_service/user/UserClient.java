//package com.webstore.order_service.user;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.Optional;
//
//@FeignClient(
//        name = "user-service",
//        url = "${application.config.users-url}"
//)
//public interface UserClient {
//
//    @GetMapping("/{user-id}")
//    Optional<UserResponse> findUserById(@PathVariable("user-id") Long userId);
//
//    @GetMapping("/{user-id}/exists")
//    boolean userExists(@PathVariable("user-id") Long userId);
//
//}
