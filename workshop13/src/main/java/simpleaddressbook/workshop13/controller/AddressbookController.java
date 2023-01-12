package simpleaddressbook.workshop13.controller;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import simpleaddressbook.workshop13.models.Contact;
import simpleaddressbook.workshop13.util.Contacts;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Controller
@RequestMapping(path = "/addressbook")
public class AddressbookController {

    //The @Autowired annotation is used in Spring to indicate that a bean property should be automatically injected with a reference to a matching bean.
    // means that when the @Autowired annotation is used, whichever method that has Contacts ctcz will automatically be called 
    @Autowired
    Contacts ctcz;

    @Autowired
    ApplicationArguments appArgs;

    @Value("${test.data.dir}")
    private String dataDir;

    // Get request, response to be returned to the addressbook.html
    @GetMapping
    public String showAddressbookForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "addressbook";
    }

    // Post request, from the form field post the data to the addressbook.html 
    // validation for all the form fields, when the user clicks on the submit button, these form validation will be activated 
    @PostMapping
    // Bind the result of the Contact to the model
    public String saveContact(@Valid Contact contact, BindingResult binding, Model model) {

        // when the fields do not fulfill the validation (aka having errors), return back to the addressbook.html (home page)
        // a default procedure to bind the result with the validation
        if (binding.hasErrors()) {
            return "addressbook";
        }

        // Checking of the age, whether the date of birth is between 10 and 100 years old
        // Step 1: have to get the date of birth from the form field
        System.out.println(checkAgeInBetween(contact.getDateOfBirth()));

        // Step 2: using the method, check whether the date of birth is between 10 and 100 years old
        if (!checkAgeInBetween(contact.getDateOfBirth())) {

            // A class in the validation framework to represent an error in an object being validated. To report an error message associated with a specific field or object.
            ObjectError err = new ObjectError("dateOfBirth", "Age must be between 10 and 100 years old");

            // bind the result using the addError method
            // in order for the error message to be displayed, it has to be binded
            binding.addError(err);

            // return back to the addressbook.html (home page)
            return "addressbook";
        }

        // call in savecontact method from util.Contacts
        ctcz.saveContact(contact, model, appArgs, dataDir);

        // return to showContact.html
        return "showContact";
    }

    @GetMapping("{contactId}")
    public String getContactById(Model model, @PathVariable String contactId) {
        // obtain all the attributes from the contact and return them onto the file
        ctcz.getContactById(model, contactId, appArgs, dataDir);
        return "showContact";
    }

    // check true or false
    private boolean checkAgeInBetween(LocalDate dob) {
        System.out.println("check age");
        int calculatedAge = 0;
        if ((dob != null)) {
            calculatedAge = Period.between(dob, LocalDate.now()).getYears();
        }
        System.out.println(calculatedAge);
        if (calculatedAge >= 10 && calculatedAge <= 100)
            return true;
        return false;

    }
}
