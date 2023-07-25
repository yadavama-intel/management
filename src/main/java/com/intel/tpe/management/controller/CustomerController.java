package com.intel.tpe.management.controller;

import com.intel.tpe.management.dataaccess.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Customer controller to store customer info
 * 1. Customer info
 * 2. All deployments for the customer
 * 3. Features enabled per deployment per customer
 * 4.
 */
@Slf4j
@RequestMapping("/customer")
@RestController
public class CustomerController {

    // Create a new customer Info
    @PostMapping("/new")
    public ResponseEntity<Customer> addNewCustomer(@RequestBody Customer customer) {
        //save to data store
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }


    // Get customer Info
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerInfo(@PathVariable String customerId) {
        log.info("getting customer by id {}", customerId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    // get all available features
}
