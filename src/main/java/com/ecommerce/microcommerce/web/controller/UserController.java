package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.UserDao;
import com.ecommerce.microcommerce.model.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Api( description="API pour es opérations CRUD sur les users.")

@RestController
public class UserController {
    @Autowired
    private UserDao userDao;

    //ajouter un produit
    @PostMapping(value = "/Users")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody User user) {

        User userAdded =  userDao.save(user);

        if (userAdded == null)
            return ResponseEntity.noContent().build();


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //Récupérer la liste des users

    @RequestMapping(value = "/Users", method = RequestMethod.GET)

    public MappingJacksonValue listeUsers() {

        Iterable<User> users = userDao.findAll();

        return new MappingJacksonValue(users);
    }

}
