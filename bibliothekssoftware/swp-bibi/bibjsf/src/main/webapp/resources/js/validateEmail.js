/**  
 * JSF Validator of emails. E-mail value must be either the empty string
 * or be matched by the given pattern.
 */  
PrimeFaces.validator['custom.emailValidator'] = {  
      
    pattern: /\S+@\S+/,  
      
    validate: function(element, value) {  
        //use element.data() to access validation metadata, in this case there is none.  
        if(value && 0 < value.length && !this.pattern.test(value)) {  
            throw {  
                summary: 'Validation Error',  
                detail: value + ' is not a valid email.'  
            };
        }
    }  
};
