// // ────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
// //                                                                                               4. Handle Validation Errors and Exceptions Globally                                                                                               

// // Action: Create a global exception handler to capture and respond to validation errors and other exceptions.                                                                                                                                     

// // Instructions:                                                                                                                                                                                                                                   

// //  • Create spring/kitchensink/src/main/java/com/example/kitchensink/exception/GlobalExceptionHandler.java                                                                                                                                        
                                                                                                                                                                                                                                                
//     package com.example.kitchensink.exception;                                                                                                                                                                                                  
                                                                                                                                                                                                                                              
//     import org.springframework.http.HttpHeaders;                                                                                                                                                                                                
//     import org.springframework.http.HttpStatus;                                                                                                                                                                                                 
//     import org.springframework.http.ResponseEntity;                                                                                                                                                                                             
//     import org.springframework.validation.FieldError;                                                                                                                                                                                           
//     import org.springframework.web.bind.MethodArgumentNotValidException;                                                                                                                                                                        
//     import org.springframework.web.bind.annotation.ControllerAdvice;                                                                                                                                                                            
//     import org.springframework.web.context.request.WebRequest;                                                                                                                                                                                  
//     import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;                                                                                                                                                
                                                                                                                                                                                                                                                
//     import java.util.HashMap;                                                                                                                                                                                                                   
//     import java.util.Map;                                                                                                                                                                                                                       
                                                                                                                                                                                                                                                
//     @ControllerAdvice                                                                                                                                                                                                                           
//     public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {                                                                                                                                                                
                                                                                                                                                                                                                                                
//         @Override                                                                                                                                                                                                                               
//         protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,                                                                                                                                       
//                                                                       HttpHeaders headers,                                                                                                                                                      
//                                                                       HttpStatus status,                                                                                                                                                        
//                                                                       WebRequest request) {                                                                                                                                                     
//             Map<String, String> errors = new HashMap<>();                                                                                                                                                                                       
//             for (FieldError error : ex.getBindingResult().getFieldErrors()) {                                                                                                                                                                   
//                 errors.put(error.getField(), error.getDefaultMessage());                                                                                                                                                                        
//             }                                                                                                                                                                                                                                   
//             return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);                                                                                                                                                                        
//         }                                                                                                                                                                                                                                       
                                                                                                                                                                                                                                                
//         // Handle other exceptions (e.g., RuntimeException)                                                                                                                                                                                     
//         @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)                                                                                                                                                       
//         public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {                                                                                                                                                                  
//             return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);                                                                                                                                                               
//         }                                                                                                                                                                                                                                       
//     }                                                                                                                                                                                                                                           
                                                                                                                                                                                                                                                

