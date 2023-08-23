package hac.controllers;

import hac.repo.Messages;
import hac.repo.MessagesRepository;
import hac.repo.User;
import hac.repo.UserRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Messages controller that handles sending messages.
 */
@Controller
public class MessagesController {
    @Resource(name = "loggedUser")
    private User userLogged;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @GetMapping("/chat/{userName}")
    public String chat(@PathVariable("userName") String userName, Model model) {
        User user = userRepository.findByUserName(userName);
        if(user==null)
            return "redirect:/error";
        User loggedInUser = userRepository.findByUserName(userLogged.getUserName());
        List<Messages> conversation = messagesRepository.findByAuthorAndReceiverOrReceiverAndAuthor(
                loggedInUser, user, loggedInUser, user);
        if (conversation != null) {
            Collections.sort(conversation, Comparator.comparing(Messages::getId));
        }
        model.addAttribute("author", loggedInUser);
        model.addAttribute("receiver", user);
        model.addAttribute("conversation", conversation);
        model.addAttribute("message", new Messages(loggedInUser, " ", user));
        return "chatPage";
    }

    @PostMapping("/send/{userName}")
    public ResponseEntity<Messages> sendMessage(@RequestBody Map<String, String> requestParams,
                              @PathVariable("userName") String userName ) {
        User author = userRepository.findByUserName(userLogged.getUserName());
        String messageStr = requestParams.get("message");
        User receiver = userRepository.findByUserName(userName);
        Messages message = new Messages(author, messageStr, receiver);
        if(message.getMessage().trim().equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        synchronized (messagesRepository) {
            messagesRepository.save(message);
        }
        List<Messages> messages = messagesRepository.findByAuthorAndReceiverOrReceiverAndAuthor(
                message.getAuthor(), message.getReceiver(),message.getReceiver(),message.getAuthor());
        return ResponseEntity.ok(message);
    }
//    @PostMapping("/send")
//    @ResponseBody
//    public ResponseEntity<Messages> sendMessage(@RequestBody @Valid Messages message, BindingResult bindingResult) {
//        if(message.getMessage().trim().equals("")) {
//            bindingResult.reject("message","empty message");
//        }
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        synchronized (messagesRepository) {
//            messagesRepository.save(message);
//        }
//        return ResponseEntity.ok(message);
//    }

    @GetMapping("/send")
    public String getSend() {
        return "redirect:/error";
    }
    @GetMapping("/retrieveMessages")
    public String getRetrieveMessages() {
        return "redirect:/error";
    }
    @PostMapping("/retrieveMessages")
    public ResponseEntity<List<Messages>> retrieveMessages(@RequestBody Map<String, String> requestParams) {
        User user = userRepository.findByUserName(requestParams.get("author"));
        User loggedInUser = userRepository.findByUserName(userLogged.getUserName());
        List<Messages> conversation = messagesRepository.findByAuthorAndReceiverOrReceiverAndAuthor(
                loggedInUser, user, loggedInUser, user);
        if (conversation != null) {
            Collections.sort(conversation, Comparator.comparing(Messages::getId));
        }
        return ResponseEntity.ok(conversation);
    }
    @ExceptionHandler({Exception.class})
    public String handleValidationExceptions(Exception ex, Model model, HttpSession httpSession) {
        // we can insert the message into the model
        model.addAttribute("error", ex.getMessage());
        //httpSession.invalidate();
        return "error";
    }
}
