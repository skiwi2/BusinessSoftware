package com.skiwi.businesssoftware.api.user

import com.skiwi.businesssoftware.CreateUserException
import com.skiwi.businesssoftware.UserService
import groovy.json.JsonBuilder
import groovy.transform.TupleConstructor
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@RequestMapping("/api/user")
class UserController {
    UserService userService

    @RequestMapping("/create_user")
    def @ResponseBody createUser(@RequestBody CreateUserMessage createUserMessage) {
        try {
            userService.createUser(*createUserMessage.values())
            def builder = new JsonBuilder()
            builder {
                success true
            }
        } catch (CreateUserException ex) {
            def builder = new JsonBuilder()
            builder {
                success false
                message ex.message
            }
        }
    }
}

@TupleConstructor
class CreateUserMessage {
    String firstName
    String middleName
    String lastName
    String email
    String password

    List values() {
        return [firstName, middleName, lastName, email, password]
    }
}