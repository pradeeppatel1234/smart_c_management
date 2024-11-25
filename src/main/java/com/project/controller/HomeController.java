package com.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.project.dao.UserRepository;
import com.project.entities.User;
import com.project.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller

public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	
     public String handler(Model model) {
		
		model.addAttribute("tittle","this is home-smart");
		return "home";
	}
	
	@RequestMapping("/about")
	
	public String about(Model model) {
			
				model.addAttribute("tittle","this is about-smart");
				
			return "about";}
	
@RequestMapping("/signup")
	
	public String signup(Model model) {
			
				model.addAttribute("tittle","this is signup-smart");
				model.addAttribute("user",new User());
			return "signup";	
}
//this handler for registering user


@RequestMapping(value="/do_register",method=RequestMethod.POST)

public String registeruser(@Valid @ModelAttribute("user")User user  , BindingResult result1,  @RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model, HttpSession session) {
		
	try {
		
		if(!agreement) {
			System.out.println("you have not agreed the terms and conditions");
			throw new Exception("you have not agreed the terms and conditions");
		}
		
		if(result1.hasErrors()) {
			System.out.println("ERROR"+result1.toString());
			model.addAttribute("user",user);
			return "signup";
		}
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("agreement"+agreement);
			System.out.println("USER"+user);
			
			User result=this.userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully registered","alert-success"));
		  return "signup";
		
	}catch(Exception e) {
		
		e.printStackTrace();
		model.addAttribute("user",user);
		session.setAttribute("message",new Message("Something Went wrong !! "+e.getMessage(), "alert-danger"));
	
	
		return "signup";}
}

//handler for custom login
@GetMapping("/signin")

public String customLogin(Model model) {
	model.addAttribute("title","Login Page");
	return "login";
}

}

