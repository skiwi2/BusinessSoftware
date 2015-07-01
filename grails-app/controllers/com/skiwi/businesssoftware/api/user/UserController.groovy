package com.skiwi.businesssoftware.api.user

import com.skiwi.businesssoftware.CreateUserException
import com.skiwi.businesssoftware.UserService
import grails.web.controllers.ControllerMethod
import groovy.json.JsonBuilder
import groovy.transform.TupleConstructor
import org.springframework.messaging.handler.annotation.MessageMapping

@MessageMapping("/api/user")
class UserController {
    UserService userService

    def index() {

    }

    @ControllerMethod
    @MessageMapping("/create_user")
    def createUser(CreateUserMessage createUserMessage) {
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