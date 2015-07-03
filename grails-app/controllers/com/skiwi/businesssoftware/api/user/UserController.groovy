package com.skiwi.businesssoftware.api.user

import com.skiwi.businesssoftware.CreateUserException
import com.skiwi.businesssoftware.LoginUserException
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

    @RequestMapping("/login_user")
    def @ResponseBody loginUser(@RequestBody LoginUserMessage loginUserMessage) {
        if (session.user) {
            def builder = new JsonBuilder()
            return builder {
                success false
                message "ALREADY_LOGGED_IN"
            }
        }

        try {
            def user = userService.loginUser(*loginUserMessage.values())
            session.user = user
            session.token = UUID.randomUUID().toString()
            def builder = new JsonBuilder()
            builder {
                success true
                token session.token
            }
        } catch (LoginUserException ex) {
            def builder = new JsonBuilder()
            builder {
                success false
                message ex.message
            }
        }
    }

    @RequestMapping("/logout_user")
    def @ResponseBody logoutUser(@RequestBody Object object) {
        if (!session.user) {
            def builder = new JsonBuilder()
            return builder {
                success false
                message "NOT_LOGGED_IN"
            }
        }

        session.user = null
        session.token = null
        def builder = new JsonBuilder()
        builder {
            success true
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

@TupleConstructor
class LoginUserMessage {
    String email
    String password

    List values() {
        return [email, password]
    }
}