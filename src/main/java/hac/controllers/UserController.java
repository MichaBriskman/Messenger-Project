package hac.controllers;

import hac.repo.Messages;
import hac.repo.MessagesRepository;
import hac.repo.User;
import hac.repo.UserRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * UserController that handles login in registration to the chatRooms.
 */
@Controller
public class UserController {

    @Resource(name = "loggedUser")
    private User userLogged;
    @Autowired
    private UserRepository repository;
    @Autowired
    private MessagesRepository messagesRepository;
    @GetMapping("/")
    public String main(User user, Model model) {
        if(userLogged.getUserName()==null || repository.findAll().size()==0)
            return "loginPage";
        return "redirect:/chatRoom";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user, Model model) {
        return "signupPage";
    }

    @PostMapping("/signup")
    public String addUser(@Valid User user,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (repository.existsByUserName(user.getUserName())) {
            bindingResult.rejectValue("userName", "error.userName", "Username already exists");
            return "signupPage";
        }
        user.setBCrypt();
        if (!user.checkPassword(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "passwords do not match");
        }
        user.setConfirm();
        if (bindingResult.hasErrors()) {
            return "signupPage";
        }
        synchronized (repository) {
            repository.save(user);
        }
        redirectAttributes.addFlashAttribute("successMessage", "User registered successfully");
        return "redirect:/";
    }
    @PostMapping("/login")
    public String login(@RequestParam("password") String rawPassword,
                        @Valid User user,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        User foundUser = repository.findByUserName(user.getUserName());
        user.setBCrypt();
        if (foundUser == null || !foundUser.checkPassword(rawPassword)) {
            bindingResult.rejectValue("password", "error.password", "username or password are not valid");
            return "loginPage";
        }
        userLogged.setUserName(user.getUserName());
        userLogged.setPassword(rawPassword);
        userLogged.setConfirmPassword(rawPassword);
        userLogged.setBCrypt();
        userLogged.setConfirm();
        return "redirect:/chatRoom";
    }
    @GetMapping("/login")
    public String redirectLogin(User user, Model model) {
        return "redirect:/";
    }

    @GetMapping("/chatRoom")
    public String showChatRoom(Model model, RedirectAttributes redirectAttributes) {
        User foundUser = userLogged;
        model.addAttribute("loggedUser", foundUser);
        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "chatRoom";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/searchUsers")
    public String redirectSearchUsers(User user, Model model) {
        return "redirect:/chatRoom";
    }
    @PostMapping("/searchUsers")
    public String searchUsers(@RequestParam("searchText") String searchText, Model model,
                              RedirectAttributes redirectAttributes) {
        List<User> users = repository.findAll();
        List<User> matchedUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getUserName().startsWith(searchText)) {
                matchedUsers.add(user);
            }
        }
        model.addAttribute("users", matchedUsers);
        User foundUser = userLogged;
        model.addAttribute("loggedUser", foundUser);
        return "chatRoom"; // Return the chatRoom.html template with the search results
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUserGet(@PathVariable("userId") long userId, Model model) {
        return "redirect:/chatRoom";
    }
    @PostMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable("userId") long userId, Model model) {
        User user = repository.findById(userId);
        repository.delete(user);
        return "redirect:/chatRoom";
    }
    @ExceptionHandler({Exception.class})
    public String handleValidationExceptions(Exception ex, Model model,HttpSession httpSession) {
        //httpSession.invalidate();
        model.addAttribute("error", ex.getMessage());
        return "error";
    }


}
