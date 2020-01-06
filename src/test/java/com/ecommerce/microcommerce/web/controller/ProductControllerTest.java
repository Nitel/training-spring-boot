package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductDao productDao;

    private Product p1 = new Product(1, "TV 4K", 500, 300);
    private Product p2 = new Product(2, "Enceinte Bose", 200, 130);
    private Product p3 = new Product(3, "Smart Cooker", 1000, 820);
    private Product p4 = new Product(4, "Chargeur", 0, 2);
    private List<Product> listeProduit;
    FilterProvider f = new SimpleFilterProvider().addFilter("userFilter", SimpleBeanPropertyFilter.serializeAll());
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void calculerMargeProduit1_OK() throws Exception {
        Mockito.when(productDao.exists(anyInt())).thenReturn(true);
        Mockito.when(productDao.findById(anyInt())).thenReturn(p1);
        mockMvc.perform(get("/Produits/calculerMargeProduit/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("200.0"));
    }

    @Test
    public void calculerMargeProduit2_OK() throws Exception {
        Mockito.when(productDao.exists(anyInt())).thenReturn(true);
        Mockito.when(productDao.findById(anyInt())).thenReturn(p2);
        mockMvc.perform(get("/Produits/calculerMargeProduit/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("70.0"));
    }

    @Test
    public void calculerMargeProduit_GratuitException() throws Exception {
        Mockito.when(productDao.exists(anyInt())).thenReturn(true);
        Mockito.when(productDao.findById(anyInt())).thenReturn(p4);
        mockMvc.perform(get("/Produits/calculerMargeProduit/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void calculerMargeProduit_NotExistException() throws Exception {
        Mockito.when(productDao.exists(anyInt())).thenReturn(false);
        Mockito.when(productDao.findById(anyInt())).thenReturn(null);
        mockMvc.perform(get("/Produits/calculerMargeProduit/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void trierProduitsParOrdreAlphabetique1_OK() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(p1);
        listeProduits.add(p2);
        listeProduits.add(p3);
        List<Product> sortedList = new ArrayList<>();
        sortedList.add(p2);
        sortedList.add(p3);
        sortedList.add(p1);
        Mockito.when(productDao.findAllByOrderByNomAsc()).thenReturn(sortedList);
        mapper.setFilterProvider(f);

        mockMvc.perform(get("/Produits/trierProduitsParOrdreAlphabetique")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sortedList).trim().replace("\\","")));

    }

    @Test
    public void trierProduitsParOrdreAlphabetiqueSameValue_OK() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(p1);
        listeProduits.add(p3);
        listeProduits.add(p3);
        List<Product> sortedList = new ArrayList<>();
        sortedList.add(p3);
        sortedList.add(p3);
        sortedList.add(p1);

        Mockito.when(productDao.findAllByOrderByNomAsc()).thenReturn(sortedList);
        mapper.setFilterProvider(f);

        mockMvc.perform(get("/Produits/trierProduitsParOrdreAlphabetique")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sortedList).trim().replace("\\","")));
    }

    @Test
    public void trierProduitsParOrdreAlphabetiqueSameCollection_OK() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(p2);
        listeProduits.add(p3);
        listeProduits.add(p1);
        List<Product> sortedList = new ArrayList<>();
        sortedList.add(p2);
        sortedList.add(p3);
        sortedList.add(p1);

        Mockito.when(productDao.findAllByOrderByNomAsc()).thenReturn(sortedList);
        mapper.setFilterProvider(f);

        mockMvc.perform(get("/Produits/trierProduitsParOrdreAlphabetique")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sortedList).trim().replace("\\","")));
    }

    @Test
    public void trierProduitsParOrdreAlphabetiqueNoCollection_NOK() throws Exception {
        Mockito.when(productDao.findAllByOrderByNomAsc()).thenReturn(listeProduit);
        mapper.setFilterProvider(f);
        String s = null;

        mockMvc.perform(get("/Produits/trierProduitsParOrdreAlphabetique")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCalculerMargeProduit_OK() throws Exception {
        List<Product> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        Mockito.when(productDao.exists(anyInt())).thenReturn(true);
        Mockito.when(productDao.findById(1)).thenReturn(p1);
        Mockito.when(productDao.findById(2)).thenReturn(p2);
        Mockito.when(productDao.findById(3)).thenReturn(p3);
        Mockito.when(productDao.findAll()).thenReturn(list);

        mockMvc.perform(get("/AdminProduits/calculerMargeProduit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"Product{id=1, nom='TV 4K', prix=500} : 200.0\",\"Product{id=2, nom='Enceinte Bose', prix=200} : 70.0\",\"Product{id=3, nom='Smart Cooker', prix=1000} : 180.0\"]"));
    }

    @Test
    public void testCalculerMargeProduitSameValue_OK() throws Exception {
        List<Product> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p2);

        Mockito.when(productDao.exists(anyInt())).thenReturn(true);
        Mockito.when(productDao.findById(1)).thenReturn(p1);
        Mockito.when(productDao.findById(2)).thenReturn(p2);
        Mockito.when(productDao.findAll()).thenReturn(list);

        mockMvc.perform(get("/AdminProduits/calculerMargeProduit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"Product{id=1, nom='TV 4K', prix=500} : 200.0\",\"Product{id=2, nom='Enceinte Bose', prix=200} : 70.0\",\"Product{id=2, nom='Enceinte Bose', prix=200} : 70.0\"]"));
    }

    @Test
    public void testCalculerMargeProduit_GratuitException() throws Exception {
        List<Product> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p4);

        Mockito.when(productDao.exists(anyInt())).thenReturn(true);
        Mockito.when(productDao.findById(1)).thenReturn(p1);
        Mockito.when(productDao.findById(2)).thenReturn(p2);
        Mockito.when(productDao.findById(4)).thenReturn(p4);
        Mockito.when(productDao.findAll()).thenReturn(list);

        mockMvc.perform(get("/AdminProduits/calculerMargeProduit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }
}
