package simpleaddressbook.workshop13.controller;


import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import simpleaddressbook.workshop13.models.Contact;

@Controller
@RequestMapping(path = "/address")
public class Addressbook {

    @Autowired
    Contact ctcz;
    
    // Get request from the form fields
    @GetMapping
    public String showAddressBookForm(Model model){
        // add the contact object to the addressbook
        model.addAttribute("contact", new Contact());
        return "address";
    }

    // always have to do a post request when creating a form
    @PostMapping
    // always have to have @valid object, bind form result when posted
    public String postAddress(@Valid Contact contact, BindingResult bindingResult, Model model){
        

        if (bindingResult.hasErrors()){
            return "address";
        }

        // need to do the validation process (checking of the date of birth)
        if(!checkAgeInBetween(contact.getDateofbirth())){
            
            ObjectError err = new ObjectError("date of birth", "please enter a valid date of birth");

            bindingResult.addError(err);

            return "address";
        }



        return "address";
    }

    public boolean checkAgeInBetween(LocalDate dateofbirth){

        // initial age = 0
        int calculatedAge = 0;
        if(dateofbirth != null){
            calculatedAge = Period.between(dateofbirth, LocalDate.now()).getYears();
        }        

        if(calculatedAge > 10 && calculatedAge <= 100){
            return true;
        } else return false;
    }
}
