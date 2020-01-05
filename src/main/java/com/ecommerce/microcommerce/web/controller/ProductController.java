package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.dao.UserDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.model.User;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Api( description="API pour es opérations CRUD sur les produits.")

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;



    //Récupérer la liste des produits
    @RequestMapping(value = "/Produits/{idUser}", method = RequestMethod.GET)
    public List<Object> listeProduits(int idUser) throws JsonProcessingException {

        Iterable<Product> produits = productDao.findAll();
        User user = userDao.findById(idUser);


        List<Object> productList = new ArrayList<>();
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter",
                !user.isAdmin()?SimpleBeanPropertyFilter.serializeAllExcept("prix", "prixAchat"):SimpleBeanPropertyFilter.serializeAll());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(filterProvider);

        for(Product p : produits) {
            productList.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p));
        }

       return productList;
    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{idUser}/{id}")
    public String afficherUnProduit(@PathVariable int id, @PathVariable int idUser ) throws JsonProcessingException {

        Product produit = productDao.findById(id);
        User user = userDao.findById(idUser);
        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter",
                !user.isAdmin()?SimpleBeanPropertyFilter.serializeAllExcept("prix", "prixAchat"):SimpleBeanPropertyFilter.serializeAll());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(filterProvider);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(produit);
    }


    //ajouter un produit
    @PostMapping(value = "/Produits")

    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        if (productAdded.getPrix() <= 0)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {

        productDao.delete(id);
    }

    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {

        productDao.save(product);
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {

        return productDao.chercherUnProduitCher(400);
    }

    @GetMapping(value = "/Produits/calculerMargeProduit/{productId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public double calculerMargeProduit(@PathVariable int productId) {
        if(!productDao.exists(productId)) throw new ProduitIntrouvableException("Le produit avec l'id " + productId + " est INTROUVABLE. Écran Bleu si je pouvais.");
        if(productDao.findById(productId).getPrix() <= 0) throw new ProduitGratuitException("Le produit avec l'id " + productId + " est GRATUIT, pas de marge possible. Écran Bleu si je pouvais.");
        return productDao.findById(productId).getPrix() - productDao.findById(productId).getPrixAchat();
    }

    @GetMapping(value = "/Produits/trierProduitsParOrdreAlphabetique", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<Product>  trierProduitsParOrdreAlphabetique() {
        return productDao.findAllByOrderByNomAsc();
    }

    @GetMapping(value = "/AdminProduits/calculerMargeProduit", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<String>  calculerMargeProduit() {
        List<Product> list = productDao.findAll();
        List<String> stringList = new ArrayList<>();
        for (Product product : list) {
            stringList.add(product.toString() + " : " + calculerMargeProduit(product.getId()));
        }
        return stringList;
    }

}
